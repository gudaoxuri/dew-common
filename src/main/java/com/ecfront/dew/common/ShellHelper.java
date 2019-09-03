/*
 * Copyright 2019. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Shell脚本操作.
 *
 * @author gudaoxuri
 */
public class ShellHelper {

    private static final Logger logger = LoggerFactory.getLogger(ShellHelper.class);

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    ShellHelper() {
    }

    /**
     * 执行入口，如执行成功，此方法只返回output(标准输出).
     *
     * @param cmd 命令，包含参数
     */
    public Resp<List<String>> execute(String cmd) {
        return execute(cmd, null);
    }

    /**
     * 执行入口，如执行成功，此方法只返回output(标准输出).
     *
     * @param cmd 命令，包含参数
     * @param env 执行环境
     */
    public Resp<List<String>> execute(String cmd, Map<String, String> env) {
        ReportHandler reportHandler = new ReportHandler() {
        };
        try {
            execute(cmd, env, null, null, true, reportHandler).get();
            if (!reportHandler.isFail()) {
                return Resp.success(reportHandler.getOutput());
            } else {
                return Resp.customFail("", reportHandler.getFailMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Execute fail: ", e);
            return Resp.customFail("", e.getMessage());
        } catch (Exception e) {
            logger.error("Execute fail: ", e);
            return Resp.customFail("", e.getMessage());
        }
    }

    /**
     * 执行入口.
     *
     * @param cmd           命令或脚本，包含参数
     * @param successFlag   成功标识，只要捕捉到此标识就视为成功，
     *                      为null时不会调用ReportHandler的success方法
     * @param progressFlag  进度标识，只要捕捉到此标识就更新进度，
     *                      格式为: progressFlag 空格 progress ,如： progress 40，
     *                      为null时不会调用ReportHandler的progress方法
     * @param returnResult  是否返回结果（输出内容，包含标准输出stdin及标准错误stderr），
     *                      为true时会返回结果到ReportHandler的complete方法中，
     *                      结果暂存于内存中，对输出内容过多的脚本需要考虑占用内存的大小
     * @param reportHandler 任务报告实例
     */
    public Future<Void> execute(String cmd,
                                String successFlag, String progressFlag,
                                boolean returnResult,
                                ReportHandler reportHandler) throws RTIOException {
        return execute(cmd, null, successFlag, progressFlag, returnResult, reportHandler);
    }

    /**
     * 执行入口.
     *
     * @param cmd           命令或脚本，包含参数
     * @param env           执行环境
     * @param successFlag   成功标识，只要捕捉到此标识就视为成功，
     *                      为null时不会调用ReportHandler的success方法
     * @param progressFlag  进度标识，只要捕捉到此标识就更新进度，
     *                      格式为: progressFlag 空格 progress ,如： progress 40，
     *                      为null时不会调用ReportHandler的progress方法
     * @param returnResult  是否返回结果（输出内容，包含标准输出stdin及标准错误stderr），
     *                      为true时会返回结果到ReportHandler的complete方法中，
     *                      结果暂存于内存中，对输出内容过多的脚本需要考虑占用内存的大小
     * @param reportHandler 任务报告实例
     */
    public Future<Void> execute(String cmd, Map<String, String> env,
                                String successFlag, String progressFlag,
                                boolean returnResult,
                                ReportHandler reportHandler) throws RTIOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (env != null && !env.isEmpty()) {
            processBuilder.environment().putAll(env);
        }
        if ($.file.isWindows()) {
            processBuilder.command("cmd.exe", "/c", cmd);
        } else {
            processBuilder.command("bash", "-c", cmd);
        }
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RTIOException(e);
        }
        return pool.submit(new ProcessReadTask(process, cmd,
                successFlag, progressFlag, returnResult, reportHandler));
    }

    private static class ProcessReadTask implements Callable<Void> {
        private Process process;
        private InputStream errorStream;
        private InputStream outputStream;
        private String cmd;
        private String successFlag;
        private String progressFlag;
        private boolean returnResult;
        private ReportHandler reportHandler;

        ProcessReadTask(Process process, String cmd, String successFlag, String progressFlag, boolean returnResult, ReportHandler reportHandler) {
            this.process = process;
            this.errorStream = process.getErrorStream();
            this.outputStream = process.getInputStream();
            this.cmd = cmd;
            this.successFlag = successFlag;
            this.progressFlag = progressFlag;
            this.returnResult = returnResult;
            this.reportHandler = reportHandler;
        }

        @Override
        public Void call() throws InterruptedException, ExecutionException {
            Future<List<String>> outputResultF = pool.submit(
                    new ProcessReader(outputStream, successFlag, progressFlag, returnResult, reportHandler, true));
            Future<List<String>> errorResultF = pool.submit(
                    new ProcessReader(errorStream, successFlag, progressFlag, returnResult, reportHandler, false));
            List<String> outputResult = outputResultF.get();
            List<String> errorResult = errorResultF.get();
            reportHandler.onComplete(outputResult, errorResult);
            return null;
        }

        private class ProcessReader implements Callable<List<String>> {

            private InputStream stream;
            private String successFlag;
            private String progressFlag;
            private boolean returnResult;
            private ReportHandler reportHandler;
            private boolean isOutput;

            ProcessReader(InputStream stream, String successFlag, String progressFlag,
                          boolean returnResult, ReportHandler reportHandler, boolean isOutput) {
                this.stream = stream;
                this.successFlag = successFlag;
                this.progressFlag = progressFlag;
                this.returnResult = returnResult;
                this.reportHandler = reportHandler;
                this.isOutput = isOutput;
            }

            @Override
            public List<String> call() throws Exception {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                List<String> result = new ArrayList<>();
                String message;
                boolean stop = false;
                try {
                    while (!stop && (message = reader.readLine()) != null) {
                        if (isOutput) {
                            reportHandler.outputlog(message);
                            logger.trace("Shell output content:" + message);
                        } else {
                            reportHandler.errorlog(message);
                            logger.trace("Shell error content:" + message);
                        }
                        if (returnResult) {
                            result.add(message);
                        }
                        if (successFlag != null
                                && message.toLowerCase().contains(successFlag.toLowerCase())) {
                            reportHandler.onSuccess();
                            stop = true;
                        }
                        if (progressFlag != null
                                && message.toLowerCase().contains(progressFlag.toLowerCase())) {
                            reportHandler.onProgress(
                                    Integer.valueOf(message.substring(message.indexOf(progressFlag) + progressFlag.length()).trim()));
                        }
                    }
                } catch (IOException e) {
                    String error = e.getMessage() + ", cmd : " + cmd
                            + "\r\n" + String.join("\r\n", result);
                    logger.error("Execute fail: " + error, e);
                    reportHandler.onFail(error);
                } finally {
                    if (0 != process.waitFor()) {
                        String error = "Abnormal termination , cmd : " + cmd
                                + "\r\n" + String.join("\r\n", result);
                        logger.warn("Execute fail: " + error);
                        reportHandler.onFail(error);
                    }
                }
                return result;
            }
        }
    }

}
