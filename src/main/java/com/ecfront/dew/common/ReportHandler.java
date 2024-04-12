package com.ecfront.dew.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务报告接口.
 *
 * @author gudaoxuri
 */
public abstract class ReportHandler {

    private boolean isSuccess = false;
    private boolean isFail = false;
    private String failMessage = "";
    private int progress = 0;
    private List<String> output = new ArrayList<>();
    private List<String> error = new ArrayList<>();

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    public final boolean isSuccess() {
        return isSuccess;
    }

    /**
     * 是否失败
     *
     * @return 是否失败
     */
    public final boolean isFail() {
        return isFail;
    }

    /**
     * 获取失败原因
     *
     * @return 失败原因
     */
    public final String getFailMessage() {
        return failMessage;
    }

    /**
     * 获取进度
     *
     * @return 进度
     */
    public final int getProgress() {
        return progress;
    }

    /**
     * 获取输出内容
     *
     * @return 输出内容
     */
    public final List<String> getOutput() {
        return output;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public final List<String> getError() {
        return error;
    }

    /**
     * 成功，在执行到successFlag时调用.
     */
    final void onSuccess() {
        this.isSuccess = true;
        success();
    }

    /**
     * 输出每行错误输出数据.
     *
     * @param line 输出行内容
     */
    public void errorlog(String line) {

    }

    /**
     * 输出每行标准输出数据.
     *
     * @param line 输出行内容
     */
    public void outputlog(String line) {

    }

    /**
     * 成功，在执行到successFlag时调用.
     */
    public void success() {

    }

    /**
     * 失败，在发生错误时调用.
     *
     * @param message 失败原因描述
     */
    final void onFail(String message) {
        this.isFail = true;
        this.failMessage = message;
        fail(message);
    }

    /**
     * 失败，在发生错误时调用.
     *
     * @param message 失败原因描述
     */
    public void fail(String message) {

    }

    /**
     * 进度回调，在执行到progressFlag且格式正确时调用.
     *
     * @param progress 0-100
     */
    final void onProgress(int progress) {
        this.progress = progress;
        progress(progress);
    }

    /**
     * 进度回调，在执行到progressFlag且格式正确时调用.
     *
     * @param progress 0-100
     */
    public void progress(int progress) {
    }

    /**
     * 完成，无论是否成功，在执行完成时(success 或 fail 方法后)调用.
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
     * 完成，无论是否成功，在执行完成时(success 或 fail 方法后)调用.
     *
     * @param output stdout结果
     * @param error  stderr结果
     */
    public void complete(List<String> output, List<String> error) {
    }

}
