package com.ecfront.dew.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务报告接口
 */
public abstract class ReportHandler {

    private boolean isSuccess = false;
    private boolean isFail = false;
    private String failMessage = "";
    private int progress = 0;
    private List<String> output = new ArrayList<>();
    private List<String> error = new ArrayList<>();

    public final boolean isSuccess() {
        return isSuccess;
    }

    public final boolean isFail() {
        return isFail;
    }

    public final String getFailMessage() {
        return failMessage;
    }

    public final int getProgress() {
        return progress;
    }

    public final List<String> getOutput() {
        return output;
    }

    public final List<String> getError() {
        return error;
    }

    /**
     * 成功，在执行到successFlag时调用
     */
    final void onSuccess() {
        this.isSuccess = true;
        success();
    }


    /**
     * 成功，在执行到successFlag时调用
     */
    void success() {

    }

    /**
     * 失败，在发生错误时调用
     *
     * @param message 失败原因描述
     */
    final void onFail(String message) {
        this.isFail = true;
        this.failMessage = message;
        fail(message);
    }

    /**
     * 失败，在发生错误时调用
     *
     * @param message 失败原因描述
     */
    void fail(String message) {

    }

    /**
     * 进度回调，在执行到progressFlag且格式正确时调用
     *
     * @param progress 0-100
     */
    final void onProgress(int progress) {
        this.progress = progress;
        progress(progress);
    }

    /**
     * 进度回调，在执行到progressFlag且格式正确时调用
     *
     * @param progress 0-100
     */
    void progress(int progress) {
    }

    /**
     * 完成，无论是否成功，在执行完成时(success 或 fail 方法后)调用
     *
     * @param output stdout标准输出结果
     * @param error  stderr标准错误结果
     */
    final void onComplete(List<String> output, List<String> error) {
        this.output = output;
        this.error = error;
        complete(output, error);
    }

    /**
     * 完成，无论是否成功，在执行完成时(success 或 fail 方法后)调用
     *
     * @param output stdout结果
     * @param error  stderr结果
     */
    void complete(List<String> output, List<String> error) {
    }


}
