package com.ecfront.dew.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Shell辅助类
 */
public class ShellHelper {

    private volatile String taskId;
    private volatile String successFlag;
    private volatile String progressFlag;
    private volatile ReportHandler reportHandler;

    private volatile StringBuffer result = new StringBuffer();
    private boolean returnResult = false;

    /**
     * 执行入口
     *
     * @param shellPath     sh文件路径，包含参数
     * @param taskId        任务ID
     * @param successFlag   成功标识，只要捕捉到此标识就视为成功
     * @param progressFlag  进度标识，只要捕捉到此标识就更新进度， 格式为 <progressFlag>空格<progress>,如： progress 40
     * @param returnResult  是否返回结果
     * @param reportHandler 任务报告实例
     */
    public void execute(String shellPath, String taskId, String successFlag, String progressFlag, boolean returnResult, ReportHandler reportHandler) {
        this.taskId = taskId;
        this.successFlag = successFlag.toLowerCase();
        this.progressFlag = progressFlag.toLowerCase();
        this.returnResult = returnResult;
        this.reportHandler = reportHandler;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shellPath});
            if (process != null) {
                StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
                StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
                Future<Boolean> errorFuture = Executors.newCachedThreadPool().submit(errorGobbler);
                Future<Boolean> outputFuture = Executors.newCachedThreadPool().submit(outputGobbler);
                if (0 == process.waitFor()) {
                    if (errorFuture.get() || outputFuture.get()) {
                        if (reportHandler != null) {
                            //删除最后一行（\r\n）
                            reportHandler.complete(taskId, result.length() > 0 ? result.substring(0, result.length() - 2) : result.toString());
                        }
                        LOGGER.debug("Execute Success: " + shellPath);
                    } else {
                        LOGGER.warn("Execute fail: Not Find successFlag [" + successFlag + "], shellPath:" + shellPath);
                        reportHandler.fail(taskId, "Not Find successFlag [" + successFlag + "], shellPath:" + shellPath);
                    }
                } else {
                    LOGGER.warn("Execute fail: Abnormal termination , shellPath:" + shellPath);
                    reportHandler.fail(taskId, "Abnormal termination , shellPath:" + shellPath);
                }
            } else {
                LOGGER.warn("Execute fail: PID NOT exist , shellPath:" + shellPath);
                reportHandler.fail(taskId, "PID NOT exist , shellPath:" + shellPath);
            }
        } catch (Exception e) {
            LOGGER.error("Execute fail: ", e);
            reportHandler.fail(taskId, e.getMessage() + " , shellPath:" + shellPath);
        }
    }

    /**
     * 输出处理
     */
    class StreamGobbler implements Callable<Boolean> {
        InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        @Override
        public Boolean call() {
            InputStreamReader isr = null;
            BufferedReader br = null;
            String line;
            try {
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    LOGGER.trace("Shell content:" + line);
                    if (returnResult) {
                        result.append(line).append("\r\n");
                    }
                    if (line.toLowerCase().contains(successFlag)) {
                        reportHandler.success(taskId);
                        return true;
                    }
                    if (line.toLowerCase().contains(progressFlag)) {
                        reportHandler.progress(taskId, Integer.valueOf(line.substring(line.indexOf(progressFlag) + progressFlag.length()).trim()));
                    }
                }
            } catch (IOException e) {
                LOGGER.warn("Execute fail: ", e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        LOGGER.warn("Execute warn: ", e);
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (Exception e) {
                        LOGGER.warn("Execute warn: ", e);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        LOGGER.warn("Execute warn: ", e);
                    }
                }
            }
            return false;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellHelper.class);

}
