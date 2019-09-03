package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * The type Fallback helper test.
 *
 * @author gudaoxuri
 */
public class FallbackHelperTest {

    /**
     * Test get user.
     */
    @Test
    public void testGetUser() {
        String result = $.fallback.execute("testGetUser",
                this::getUserNormal,
                this::getUserError);
        Assert.assertEquals("-1", result);
    }

    /**
     * Test get user with strategy.
     */
    @Test
    public void testGetUserWithStrategy() {
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals("-1", getUserWithStrategy());
        }
        Assert.assertEquals("-2", getUserWithStrategy());
        Assert.assertEquals("-2", getUserWithStrategy());
        Assert.assertEquals("-2", getUserWithStrategy());
        Assert.assertEquals("-2", getUserWithStrategy());
        Assert.assertEquals("-2", getUserWithStrategy());
        Assert.assertEquals("-1", getUserWithStrategy());
    }

    private String getUserWithStrategy() {
        return $.fallback.execute("testGetUserWithStrategy",
                this::getUserNormal,
                this::getUserError, info -> {
                    if (info.getRequestTimes() - info.getSuccessTimes() > 15) {
                        info.setStatus(FallbackHelper.FallbackStatus.GREEN);
                    } else if (info.getRequestTimes() - info.getSuccessTimes() > 10) {
                        info.setStatus(FallbackHelper.FallbackStatus.YELLOW);
                        return false;
                    }
                    return true;
                });
    }

    /**
     * Test get user without some exception.
     */
    @Test
    public void testGetUserWithoutSomeException() {
        try {
            $.fallback.execute("testGetUserWithoutSomeException",
                    this::getUserNormal,
                    this::getUserError, NotFoundException.class);
        } catch (Throwable e) {
            if (e.getCause().getClass() != NotFoundException.class) {
                Assert.fail();
            }
        }
    }

    private String getUserNormal() throws Exception {
        throw new NotFoundException("Not Found xxx");
    }


    private String getUserError(Throwable e, FallbackHelper.FallbackInfo fallbackInfo) {
        return e.getClass() == FallbackHelper.FallbackException.class ? "-2" : "-1";
    }

    /**
     * The type Not found exception.
     */
    public static class NotFoundException extends Exception {
        /**
         * Instantiates a new Not found exception.
         *
         * @param message the message
         */
        public NotFoundException(String message) {
            super(message);
        }
    }


}
