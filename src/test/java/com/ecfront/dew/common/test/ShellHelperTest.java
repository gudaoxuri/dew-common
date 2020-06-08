/*
 * Copyright 2020. the original author or authors.
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

package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.ReportHandler;
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
    // @Test
    public void testENV() throws InterruptedException, ExecutionException {
        $.shell.execute("cd C:\\Users\\i\\OneDrive\\workspaces\\2.jobs\\message-devops-test\\terminal\\captcha\\ && npm run build:uat",
                new HashMap<>() {
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
     */
    @Test
    public void test() {
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
    }

    /**
     * Test bash.
     * <p>
     * NOTE: travis-ci 中执行有问题
     *
     * @throws InterruptedException the interrupted exception
     */
    // @Test
    public void testBash() throws InterruptedException {
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
