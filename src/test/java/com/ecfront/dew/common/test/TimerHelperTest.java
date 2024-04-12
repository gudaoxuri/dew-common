package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Timer helper test.
 *
 * @author gudaoxuri
 */
public class TimerHelperTest {

    /**
     * Test timer.
     *
     * @throws Exception the exception
     */
    @Test
    public void testTimer() throws Exception {
        int[] i = {0};
        $.timer.timer(1, () -> Assertions.assertEquals(1, i[0]));
        i[0] = 1;

        String taskId = $.timer.periodic(1, true, () -> i[0]++);
        Thread.sleep(1500);
        $.timer.cancel(taskId);
        Thread.sleep(2000);
        Assertions.assertEquals(3, i[0]);

    }

}
