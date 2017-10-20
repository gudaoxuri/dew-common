package com.ecfront.dew.common;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class ReturnPerfTest {

    public static String getUserByUserIdLimitByAppIdWithException(String userId, String appId) throws InvokeException {
        throw new InvokeException("10001", "Some Error");
    }

    public static Resp<String> getUserByUserIdLimitByAppIdWithResp(String userId, String appId) {
        return Resp.customFail("10001", "Some Error");
    }

    private static class InvokeException extends Exception {
        public InvokeException(String code, String message) {
            super("{\"code\":\"" + code + "\",\"message\":\"" + message + "\"}");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicLong exceptionCounter = new AtomicLong();
        AtomicLong respCounter = new AtomicLong();
        Thread exceptionThread = new Thread(() -> {
            while (true) {
                try {
                    getUserByUserIdLimitByAppIdWithException("", "");
                } catch (InvokeException ignored) {
                }
                exceptionCounter.incrementAndGet();
            }
        });
        Thread respThread = new Thread(() -> {
            while (true) {
                getUserByUserIdLimitByAppIdWithResp("", "");
                respCounter.incrementAndGet();
            }
        });
        long timestamp = Instant.now().toEpochMilli();
        exceptionThread.start();
        respThread.start();
        while (true){
            Thread.sleep(1000);
            System.out.println(String.format("Elapsed time %ss -> exception count %sw | resp count %sw",(Instant.now().toEpochMilli()-timestamp)/1000,exceptionCounter.get()/10000,respCounter.get()/10000));
        }
    }

}
