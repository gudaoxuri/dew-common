package com.ecfront.dew.common;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerHelper {

    private static ScheduledThreadPoolExecutor ex = new ScheduledThreadPoolExecutor(1);

    public static void periodic(long initialDelay, long period, Runnable fun) {
        ex.scheduleAtFixedRate(fun, initialDelay, period, TimeUnit.SECONDS);
    }

    public static void periodic(long period, Runnable fun) {
        periodic(0, period, fun);
    }

    public static void timer(long delay, Runnable fun) {
        ex.schedule(fun, delay, TimeUnit.SECONDS);
    }

}
