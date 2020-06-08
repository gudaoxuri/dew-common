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

import com.ecfront.dew.common.Resp;
import com.ecfront.dew.common.exception.RTException;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Return perf test.
 *
 * @author gudaoxuri
 */
public class ReturnPerfTest {

    /**
     * Gets user by user id limit by app id with exception.
     *
     * @param userId the user id
     * @param appId  the app id
     * @return the user by user id limit by app id with exception
     * @throws InvokeException the invoke exception
     */
    public static String getUserByUserIdLimitByAppIdWithException(String userId, String appId) throws InvokeException {
        throw new InvokeException("10001", "Some Error");
    }

    /**
     * Gets user by user id limit by app id with resp.
     *
     * @param userId the user id
     * @param appId  the app id
     * @return the user by user id limit by app id with resp
     */
    public static Resp<String> getUserByUserIdLimitByAppIdWithResp(String userId, String appId) {
        return Resp.customFail("10001", "Some Error");
    }

    private static class InvokeException extends Exception {
        /**
         * Instantiates a new Invoke exception.
         *
         * @param code    the code
         * @param message the message
         */
        public InvokeException(String code, String message) {
            super("{\"code\":\"" + code + "\",\"message\":\"" + message + "\"}");
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        AtomicLong exceptionCounter = new AtomicLong();
        AtomicLong respCounter = new AtomicLong();
        Thread exceptionThread = new Thread(() -> {
            while (true) {
                try {
                    getUserByUserIdLimitByAppIdWithException("", "");
                } catch (InvokeException ignored) {
                    throw new RTException(ignored);
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
        while (true) {
            Thread.sleep(1000);
            System.out.println(
                    String.format("Elapsed time %ss -> exception count %sw | resp count %sw",
                            (Instant.now().toEpochMilli() - timestamp) / 1000, exceptionCounter.get() / 10000, respCounter.get() / 10000));
        }
    }

}
