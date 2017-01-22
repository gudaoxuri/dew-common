package com.ecfront.dew.common;

/**
 * 任务报告接口
 */
public interface ReportHandler {

    /**
     * 成功
     *
     * @param taskId 任务ID
     */
    void success(String taskId);

    /**
     * 失败
     *
     * @param taskId  任务ID
     * @param message 失败原因描述
     */
    void fail(String taskId, String message);

    /**
     * 进度
     *
     * @param taskId   任务ID
     * @param progress 0-100
     */
    void progress(String taskId, int progress);

    /**
     * 完成
     *
     * @param taskId 任务ID
     * @param result 结果
     */
    void complete(String taskId,String result);

}
