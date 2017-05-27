package com.ecfront.dew.common;

import org.junit.Test;

public class ShellHelperTest {

    @Test
    public void testShell() throws Exception {
        new ShellHelper().execute(
                "/opt/test.sh", //  sh文件路径，包含参数
                "test1", // 任务ID
                "done!", // 成功标识，只要捕捉到此标识就视为成功，为null时不会调用ReportHandler的success方法，执行结束后会调用ReportHandler的fail方法
                "step", // 进度标识，只要捕捉到此标识就更新进度，格式为 <progressFlag>空格<progress>,如： progress 40，为null时不会调用ReportHandler的progress方法
                false, // 是否返回结果（输出内容），为true时会返回结果到ReportHandler的complete方法中，结果暂存于内存中，对输出内容过多的脚本需要考虑占用内存的大小
                new ReportHandler() { // 任务报告实例
                    @Override
                    public void success(String taskId) { // 成功，在执行到successFlag时调用
                    }

                    @Override
                    public void fail(String taskId, String message) { // 失败，在执行完成且successFlag不存在或发生错误时调用
                    }

                    @Override
                    public void progress(String taskId, int progress) { // 进度回调，在执行到progressFlag且格式正确时调用
                    }

                    @Override
                    public void complete(String taskId, String result) { // 完成，无法是否成功，在执行完成时调用
                    }
                }
        );
    }

}