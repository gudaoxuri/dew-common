package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class TimerHelperTest {

    @Test
    public void testTimer() throws Exception {
        int[] i = {0};
        $.timer.timer(1, () -> Assert.assertEquals(1, i[0]));
        i[0] = 1;

        String taskId = $.timer.periodic(1, true, () -> i[0]++);
        Thread.sleep(1500);
        $.timer.cancel(taskId);
        Thread.sleep(2000);
        Assert.assertEquals(3, i[0]);

    }

}