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

package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的降级处理工具.
 *
 * @author gudaoxuri
 */
public class FallbackHelper {

    // 要做降级处理的集合
    private static final Map<String, FallbackInfo> CONTAINER = new ConcurrentHashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    /**
     * 根据组名获取降级信息.
     *
     * @param groupName 组名，全局唯一
     * @return 降级信息 fallback info
     */
    public FallbackInfo getFallbackInfo(String groupName) {
        return CONTAINER.getOrDefault(groupName, new FallbackInfo());
    }

    /**
     * 带降级的处理方法.
     *
     * @param <E>             the type parameter
     * @param groupName       组名，全局唯一
     * @param normalProcessor 正常方法，抛异常时进入失败方法
     * @param errorProcessor  失败方法
     * @return the e
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor) {
        return execute(groupName, normalProcessor, errorProcessor, new DefaultFallbackStrategy(), new ArrayList<>());
    }

    /**
     * 带降级的处理方法.
     *
     * @param <E>              the type parameter
     * @param groupName        组名，全局唯一
     * @param normalProcessor  正常方法，抛异常时进入失败方法
     * @param errorProcessor   失败方法
     * @param fallbackStrategy 降级策略
     * @return the e
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                         FallbackStrategy fallbackStrategy) {
        return execute(groupName, normalProcessor, errorProcessor, fallbackStrategy, new ArrayList<>());
    }

    /**
     * 带降级的处理方法.
     *
     * @param <E>                   the type parameter
     * @param groupName             组名，全局唯一
     * @param normalProcessor       正常方法，抛异常时进入失败方法
     * @param errorProcessor        失败方法
     * @param excludeFallbackErrors 不做降级处理的异常集合（视为业务上正常的错误）
     * @return the e
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                         Class<? extends Throwable>... excludeFallbackErrors) {
        return execute(groupName, normalProcessor, errorProcessor, new DefaultFallbackStrategy(), Arrays.asList(excludeFallbackErrors));
    }

    /**
     * 带降级的处理方法.
     *
     * @param <E>                   the type parameter
     * @param groupName             组名，全局唯一
     * @param normalProcessor       正常方法，抛异常时进入失败方法
     * @param errorProcessor        失败方法
     * @param fallbackStrategy      降级策略
     * @param excludeFallbackErrors 不做降级处理的异常集合（视为业务上正常的错误）
     * @return the e
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                         FallbackStrategy fallbackStrategy, Class<? extends Throwable>... excludeFallbackErrors) {
        return execute(groupName, normalProcessor, errorProcessor, fallbackStrategy, Arrays.asList(excludeFallbackErrors));
    }

    /**
     * 带降级的处理方法.
     *
     * @param groupName             组名，全局唯一
     * @param normalProcessor       正常方法，抛异常时进入失败方法
     * @param errorProcessor        失败方法
     * @param fallbackStrategy      降级策略
     * @param excludeFallbackErrors 不做降级处理的异常集合（视为业务上正常的错误）
     */
    private <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                          FallbackStrategy fallbackStrategy, List<Class<? extends Throwable>> excludeFallbackErrors) {
        CONTAINER.putIfAbsent(groupName, new FallbackInfo());
        FallbackInfo fallbackInfo = CONTAINER.get(groupName);
        fallbackInfo.request();
        if (fallbackStrategy.check(fallbackInfo)) {
            try {
                E result = (E) normalProcessor.execute();
                fallbackInfo.success();
                return result;
            } catch (Throwable e) {
                if (!excludeFallbackErrors.contains(e.getClass())) {
                    fallbackInfo.error();
                    return (E) errorProcessor.execute(e, fallbackInfo);
                } else {
                    fallbackInfo.success();
                    throw new RTException(e);
                }
            }
        } else {
            return (E) errorProcessor.execute(
                    new FallbackException(String.format("%s has %s errors , last success is %s",
                            groupName,
                            fallbackInfo.getErrorTimes(),
                            fallbackInfo.getLastGreenTime().format(DATE_TIME_FORMATTER))),
                    fallbackInfo);
        }
    }

    /**
     * 降级信息.
     */
    public static class FallbackInfo {

        private FallbackStatus status = FallbackStatus.GREEN;
        private LocalDateTime lastGreenTime = LocalDateTime.now();
        private LocalDateTime lastYellowTime = null;
        private LocalDateTime lastRedTime = null;
        private AtomicInteger requestTimes = new AtomicInteger(0);
        private AtomicInteger successTimes = new AtomicInteger(0);
        private AtomicInteger errorTimes = new AtomicInteger(0);

        /**
         * Sets status.
         *
         * @param status the status
         */
        public void setStatus(FallbackStatus status) {
            this.status = status;
            switch (status) {
                case GREEN:
                    initLastGreenTime();
                    initRequestTimes();
                    initSuccessTimes();
                    initErrorTimes();
                    break;
                case YELLOW:
                    initLastYellowTime();
                    break;
                case RED:
                    initLastRedTime();
                    break;
                default:
            }
        }

        /**
         * Request.
         */
        void request() {
            requestTimes.incrementAndGet();
        }

        /**
         * Success.
         */
        void success() {
            successTimes.incrementAndGet();
        }

        /**
         * Error.
         */
        void error() {
            errorTimes.incrementAndGet();
        }

        /**
         * Init request times.
         */
        void initRequestTimes() {
            requestTimes = new AtomicInteger(0);
        }

        /**
         * Init success times.
         */
        void initSuccessTimes() {
            successTimes = new AtomicInteger(0);
        }

        /**
         * Init error times.
         */
        void initErrorTimes() {
            errorTimes = new AtomicInteger(0);
        }

        /**
         * Init last green time.
         */
        void initLastGreenTime() {
            this.lastGreenTime = LocalDateTime.now();
        }

        /**
         * Init last yellow time.
         */
        void initLastYellowTime() {
            this.lastYellowTime = LocalDateTime.now();
        }

        /**
         * Init last red time.
         */
        void initLastRedTime() {
            this.lastRedTime = LocalDateTime.now();
        }

        /**
         * Gets status.
         *
         * @return the status
         */
        public FallbackStatus getStatus() {
            return status;
        }

        /**
         * Gets request times.
         *
         * @return the request times
         */
        public int getRequestTimes() {
            return requestTimes.intValue();
        }

        /**
         * Gets success times.
         *
         * @return the success times
         */
        public int getSuccessTimes() {
            return successTimes.intValue();
        }

        /**
         * Gets error times.
         *
         * @return the error times
         */
        public int getErrorTimes() {
            return errorTimes.intValue();
        }

        /**
         * Gets last green time.
         *
         * @return the last green time
         */
        public LocalDateTime getLastGreenTime() {
            return lastGreenTime;
        }

        /**
         * Gets last yellow time.
         *
         * @return the last yellow time
         */
        public LocalDateTime getLastYellowTime() {
            return lastYellowTime;
        }

        /**
         * Gets last red time.
         *
         * @return the last red time
         */
        public LocalDateTime getLastRedTime() {
            return lastRedTime;
        }
    }

    /**
     * The enum Fallback status.
     */
    public enum FallbackStatus {
        /**
         * Green fallback status.
         */
        GREEN,
        /**
         * Yellow fallback status.
         */
        YELLOW,
        /**
         * Red fallback status.
         */
        RED
    }

    /**
     * The type Fallback exception.
     */
    public static class FallbackException extends RTException {

        /**
         * Instantiates a new Fallback exception.
         *
         * @param message the message
         */
        public FallbackException(String message) {
            super(message);
        }

    }

    /**
     * 降级策略.
     *
     * @author gudaoxuri
     */
    @FunctionalInterface
    public interface FallbackStrategy {

        /**
         * Check boolean.
         *
         * @param info the info
         * @return the boolean
         */
        boolean check(FallbackInfo info);

    }

    /**
     * 默认的降级策略.
     *
     * @author gudaoxuri
     */
    public static class DefaultFallbackStrategy implements FallbackStrategy {

        private static final Random EXECUTE_RANDOM = new Random();

        private double yellowRatio = 1;
        private double redRatio = 5;

        /**
         * Instantiates a new Default fallback strategy.
         */
        public DefaultFallbackStrategy() {
        }

        /**
         * Instantiates a new Default fallback strategy.
         *
         * @param yellowRatio the yellow ratio
         * @param redRatio    the red ratio
         */
        public DefaultFallbackStrategy(double yellowRatio, double redRatio) {
            this.yellowRatio = yellowRatio;
            this.redRatio = redRatio;
        }

        @Override
        public boolean check(FallbackInfo info) {
            double errorRatio = info.getErrorTimes() * 1.0 / info.getRequestTimes() * 100;
            if (errorRatio > redRatio) {
                info.setStatus(FallbackStatus.RED);
            } else if (errorRatio > yellowRatio) {
                info.setStatus(FallbackStatus.YELLOW);
            } else {
                info.setStatus(FallbackStatus.GREEN);
            }
            switch (info.getStatus()) {
                case RED:
                    // 请求的1%
                    return EXECUTE_RANDOM.nextInt(info.getRequestTimes() * 100) <= info.getRequestTimes();
                case YELLOW:
                    // 请求的10%
                    return EXECUTE_RANDOM.nextInt(info.getRequestTimes() * 100) <= info.getRequestTimes() * 2;
                default:
                    return true;
            }
        }
    }

    /**
     * The interface Error processor.
     *
     * @param <E> the type parameter
     */
    @FunctionalInterface
    public interface ErrorProcessor<E> {

        /**
         * Execute e.
         *
         * @param e            the e
         * @param fallbackInfo the fallback info
         * @return the e
         */
        E execute(Throwable e, FallbackHelper.FallbackInfo fallbackInfo);

    }

    /**
     * The interface Normal processor.
     *
     * @param <E> the type parameter
     */
    @FunctionalInterface
    public interface NormalProcessor<E> {

        /**
         * Execute e.
         *
         * @return the e
         * @throws Throwable the throwable
         */
        E execute() throws Throwable;

    }


}
