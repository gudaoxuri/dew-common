package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

public class TimerHelperTest {

    @Test
    public void testTimer() throws Exception {
        int[] i = {0};
        DEW.timer.timer(1, () -> Assert.assertEquals(1, i[0]));
        i[0] = 1;

        String taskId = DEW.timer.periodic(1, true, () -> i[0]++);
        Thread.sleep(1500);
        DEW.timer.cancel(taskId);
        Thread.sleep(2000);
        Assert.assertEquals(3, i[0]);
    }


}