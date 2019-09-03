package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * The type Shell helper test.
 *
 * @author gudaoxuri
 */
public class ShellHelperTest {

    /**
     * Test env.
     *
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     */
    //@Test
    public void testENV() throws InterruptedException, ExecutionException {
        $.shell.execute("cd C:\\Users\\i\\OneDrive\\workspaces\\2.jobs\\message-devops-test\\terminal\\captcha\\ && npm run build:uat",
                new HashMap<String, String>() {
                    {
                        put("NODE_ENV", "uat");
                    }
                }, null, null, false, new ReportHandler() {
                    @Override
                    public void outputlog(String line) {
                        System.out.println("O:" + line);
                    }

                    @Override
                    public void fail(String message) {
                        System.out.println("F:" + message);
                    }

                    @Override
                    public void errorlog(String line) {
                        System.out.println("E:" + line);
                    }
                }).get();
    }

    /**
     * Test.
     *
     * @throws InterruptedException the interrupted exception
     */
    // travis-ci 中执行有问题
    // @Test
    public void test() throws InterruptedException {
        // Error test
        Assert.assertTrue($.shell.execute("abc").getMessage().contains("Abnormal termination"));
        // Normal test
        List<String> result;
        if ($.file.isWindows()) {
            result = $.shell.execute("dir").getBody();
        } else {
            result = $.shell.execute("ls -l").getBody();
        }
        Assert.assertTrue(result.stream().anyMatch(i -> i.contains("README.adoc")));
        result = $.shell.execute("git remote -v").getBody();
        Assert.assertTrue(result.stream().anyMatch(i -> i.contains("dew-common.git")));

        if (!$.file.isWindows()) {
            final List<String> statusResult = new ArrayList<>();
            CountDownLatch cdl = new CountDownLatch(1);
            String testFilePath = this.getClass().getResource("/").getPath();
            $.shell.execute(
                    testFilePath + "shell-test.sh", // 执行脚本
                    "done!", // 成功标识，只要捕捉到此标识就视为成功，为null时不会调用ReportHandler的success方法
                    "step", // 进度标识，只要捕捉到此标识就更新进度，格式为 <progressFlag>空格<progress>,如： progress 40，为null时不会调用ReportHandler的progress方法
                    true, // 是否返回结果（输出内容，包含标准输出stdout及错误输出stderr），为true时会返回结果到ReportHandler的complete方法中，结果暂存于内存中，对输出内容过多的脚本需要考虑占用内存的大小
                    new ReportHandler() {

                        @Override
                        public void success() {
                            statusResult.add("Success");
                        }

                        @Override
                        public void fail(String message) {
                            statusResult.add("Fail:" + message);
                        }

                        @Override
                        public void progress(int progress) {
                            statusResult.add("Progress:" + progress);
                        }

                        @Override
                        public void complete(List<String> output, List<String> error) {
                            Assert.assertEquals("Start", statusResult.get(0));
                            Assert.assertEquals("Progress:10", statusResult.get(1));
                            Assert.assertEquals("Progress:70", statusResult.get(2));
                            Assert.assertEquals("Success", statusResult.get(3));

                            Assert.assertEquals("Hello", output.get(1));
                            Assert.assertEquals("World", output.get(3));

                            Assert.assertEquals("some error", error.get(0));
                            cdl.countDown();
                        }
                    });
            statusResult.add("Start");
            cdl.await();
        }
    }

}
