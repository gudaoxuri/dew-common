package com.ecfront.dew.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class FPHelper {

    private static Logger logger = LoggerFactory.getLogger(FPHelper.class);

    public static <T> T uncheckCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            silentThrow(e);
            return null;
        }
    }

    public static void uncheckRun(RunnableExc r) {
        try {
            r.run();
        } catch (Exception e) {
            silentThrow(e);
        }
    }

    public interface RunnableExc {
        void run() throws Exception;
    }

    public static void silentThrow(Throwable e) {
        logger.error("", e);
    }

}
