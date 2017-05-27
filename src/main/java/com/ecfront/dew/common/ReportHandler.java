package com.ecfront.dew.common;

/**
 * 任务报告接口
 */
public interface ReportHandler {

    /**
     * 成功，在执行到successFlag时调用
     *
     * @param taskId 任务ID
     */
    void success(String taskId);

    /**
     * 失败，在执行完成且successFlag不存在或发生错误时调用
     *
     * @param taskId  任务ID
     * @param message 失败原因描述
     */
    void fail(String taskId, String message);

    /**
     * 进度回调，在执行到progressFlag且格式正确时调用
     *
     * @param taskId   任务ID
     * @param progress 0-100
     */
    void progress(String taskId, int progress);

    /**
     * 完成，无法是否成功，在执行完成时调用
     *
     * @param taskId 任务ID
     * @param result 结果，在returnResult为true时才有值
     */
    void complete(String taskId, String result);

}
