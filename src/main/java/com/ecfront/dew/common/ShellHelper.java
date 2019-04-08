package com.ecfront.dew.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Shell脚本操作
 */
public class ShellHelper {

    private static final Logger logger = LoggerFactory.getLogger(ShellHelper.class);

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    ShellHelper() {
    }


    /**
     * 执行入口，如执行成功，此方法只返回output(标准输出)
     *
     * @param cmd 命令，包含参数
     */
    public Resp<List<String>> execute(String cmd) {
        ReportHandler reportHandler = new ReportHandler() {
        };
        try {
            execute(cmd, null, null, true, false, reportHandler).get();
            if (!reportHandler.isFail()) {
                return Resp.success(reportHandler.getOutput());
            } else {
                return Resp.customFail("", reportHandler.getFailMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Execute fail: ", e);
            return Resp.customFail("", e.getMessage());
        } catch (IOException | ExecutionException e) {
            logger.error("Execute fail: ", e);
            return Resp.customFail("", e.getMessage());
        }
    }

    /**
     * 执行入口
     *
     * @param cmd              命令或脚本，包含参数
     * @param successFlag      成功标识，只要捕捉到此标识就视为成功，
     *                         为null时不会调用ReportHandler的success方法
     * @param progressFlag     进度标识，只要捕捉到此标识就更新进度，
     *                         格式为 <progressFlag>空格<progress>,如： progress 40，
     *                         为null时不会调用ReportHandler的progress方法
     * @param returnResult     是否返回结果（输出内容，包含标准输出stdin及标准错误stderr），
     *                         为true时会返回结果到ReportHandler的complete方法中，
     *                         结果暂存于内存中，对输出内容过多的脚本需要考虑占用内存的大小
     * @param fetchErrorResult 是否返回标准错误stderr输出
     * @param reportHandler    任务报告实例
     */
    public Future<Void> execute(String cmd,
                                String successFlag, String progressFlag,
                                boolean returnResult, boolean fetchErrorResult,
                                ReportHandler reportHandler) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if ($.file.isWindows()) {
            processBuilder.command("cmd.exe", "/c", cmd);
        } else {
            processBuilder.command("bash", "-c", cmd);
        }
        Process process = processBuilder.start();
        return pool.submit(new ProcessReadTask(process, cmd,
                successFlag, progressFlag, returnResult, fetchErrorResult, reportHandler));
    }

    private static class ProcessReadTask implements Callable<Void> {
        private Process process;
        private InputStream errorStream;
        private InputStream outputStream;
        private String cmd;
        private String successFlag;
        private String progressFlag;
        private boolean returnResult;
        private boolean fetchErrorResult;
        private ReportHandler reportHandler;

        ProcessReadTask(Process process, String cmd, String successFlag, String progressFlag, boolean returnResult, boolean fetchErrorResult, ReportHandler reportHandler) {
            this.process = process;
            this.errorStream = process.getErrorStream();
            this.outputStream = process.getInputStream();
            this.cmd = cmd;
            this.successFlag = successFlag;
            this.progressFlag = progressFlag;
            this.returnResult = returnResult;
            this.fetchErrorResult = fetchErrorResult;
            this.reportHandler = reportHandler;
        }

        @Override
        public Void call() throws InterruptedException {
            List<String> errorResult = new ArrayList<>();
            List<String> outputResult = new ArrayList<>();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(outputStream));
            String errorLine = null;
            String outputLine;
            boolean stop = false;
            try {
                while (!stop
                        && ((fetchErrorResult && (errorLine = errorReader.readLine()) != null)
                        | (outputLine = outputReader.readLine()) != null)) {
                    if (errorLine != null) {
                        reportHandler.errorlog(errorLine);
                        logger.trace("Shell error content:" + errorLine);
                        if (returnResult) {
                            errorResult.add(errorLine);
                        }
                    }
                    if (outputLine != null) {
                        reportHandler.outputlog(outputLine);
                        logger.trace("Shell output content:" + outputLine);
                        if (returnResult) {
                            outputResult.add(outputLine);
                        }
                    }
                    if (successFlag != null
                            && outputLine != null
                            && outputLine.toLowerCase().contains(successFlag.toLowerCase())) {
                        reportHandler.onSuccess();
                        stop = true;
                    }
                    if (progressFlag != null
                            && outputLine != null
                            && outputLine.toLowerCase().contains(progressFlag.toLowerCase())) {
                        reportHandler.onProgress(Integer.valueOf(outputLine.substring(outputLine.indexOf(progressFlag) + progressFlag.length()).trim()));
                    }
                }
            } catch (IOException e) {
                String message = e.getMessage() + ", cmd : " + cmd
                        + "\r\n" + String.join("\r\n", errorResult);
                logger.error("Execute fail: " + message, e);
                reportHandler.onFail(message);
            } finally {
                if (0 != process.waitFor()) {
                    String message = "Abnormal termination , cmd : " + cmd
                            + "\r\n" + String.join("\r\n", errorResult);
                    logger.warn("Execute fail: " + message);
                    reportHandler.onFail(message);
                }
                reportHandler.onComplete(outputResult, errorResult);
            }
            return null;
        }
    }

}
