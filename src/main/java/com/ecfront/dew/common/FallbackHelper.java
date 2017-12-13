package com.ecfront.dew.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的降级处理工具
 */
public class FallbackHelper {

    // 要做降级处理的集合
    private static final Map<String, FallbackInfo> CONTAINER = new ConcurrentHashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    /**
     * 根据组名获取降级信息
     *
     * @param groupName 组名，全局唯一
     * @return 降级信息
     */
    public FallbackInfo getFallbackInfo(String groupName) {
        return CONTAINER.getOrDefault(groupName, new FallbackInfo());
    }

    /**
     * 带降级的处理方法
     *
     * @param groupName       组名，全局唯一
     * @param normalProcessor 正常方法，抛异常时进入失败方法
     * @param errorProcessor  失败方法
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor) {
        return execute(groupName, normalProcessor, errorProcessor, new DefaultFallbackStrategy(), new ArrayList<>());
    }

    /**
     * 带降级的处理方法
     *
     * @param groupName        组名，全局唯一
     * @param normalProcessor  正常方法，抛异常时进入失败方法
     * @param errorProcessor   失败方法
     * @param fallbackStrategy 降级策略
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                         FallbackStrategy fallbackStrategy) {
        return execute(groupName, normalProcessor, errorProcessor, fallbackStrategy, new ArrayList<>());
    }

    /**
     * 带降级的处理方法
     *
     * @param groupName             组名，全局唯一
     * @param normalProcessor       正常方法，抛异常时进入失败方法
     * @param errorProcessor        失败方法
     * @param excludeFallbackErrors 不做降级处理的异常集合（视为业务上正常的错误）
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                         Class<? extends Throwable>... excludeFallbackErrors) {
        return execute(groupName, normalProcessor, errorProcessor, new DefaultFallbackStrategy(), Arrays.asList(excludeFallbackErrors));
    }

    /**
     * 带降级的处理方法
     *
     * @param groupName             组名，全局唯一
     * @param normalProcessor       正常方法，抛异常时进入失败方法
     * @param errorProcessor        失败方法
     * @param fallbackStrategy      降级策略
     * @param excludeFallbackErrors 不做降级处理的异常集合（视为业务上正常的错误）
     */
    public <E> E execute(String groupName, NormalProcessor normalProcessor, ErrorProcessor errorProcessor,
                         FallbackStrategy fallbackStrategy, Class<? extends Throwable>... excludeFallbackErrors) {
        return execute(groupName, normalProcessor, errorProcessor, fallbackStrategy, Arrays.asList(excludeFallbackErrors));
    }

    /**
     * 带降级的处理方法
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
                    throw new RuntimeException(e);
                }
            }
        } else {
            return (E) errorProcessor.execute(
                    new FallbackException(String.format("%s has %s errors , last success is %s",
                            groupName,
                            fallbackInfo.getErrorTimes(),
                            fallbackInfo.getLastGreenTime().format(DATE_TIME_FORMATTER)))
                    , fallbackInfo);
        }
    }

    /**
     * 降级信息
     */
    public static class FallbackInfo {

        private FallbackStatus status = FallbackStatus.GREEN;
        private LocalDateTime lastGreenTime = LocalDateTime.now();
        private LocalDateTime lastYellowTime = null;
        private LocalDateTime lastRedTime = null;
        private AtomicInteger requestTimes = new AtomicInteger(0);
        private AtomicInteger successTimes = new AtomicInteger(0);
        private AtomicInteger errorTimes = new AtomicInteger(0);

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

        void request() {
            requestTimes.incrementAndGet();
        }

        void success() {
            successTimes.incrementAndGet();
        }

        void error() {
            errorTimes.incrementAndGet();
        }

        void initRequestTimes() {
            requestTimes = new AtomicInteger(0);
        }

        void initSuccessTimes() {
            successTimes = new AtomicInteger(0);
        }

        void initErrorTimes() {
            errorTimes = new AtomicInteger(0);
        }

        void initLastGreenTime() {
            this.lastGreenTime = LocalDateTime.now();
        }

        void initLastYellowTime() {
            this.lastYellowTime = LocalDateTime.now();
        }

        void initLastRedTime() {
            this.lastRedTime = LocalDateTime.now();
        }

        public FallbackStatus getStatus() {
            return status;
        }

        public int getRequestTimes() {
            return requestTimes.intValue();
        }

        public int getSuccessTimes() {
            return successTimes.intValue();
        }

        public int getErrorTimes() {
            return errorTimes.intValue();
        }

        public LocalDateTime getLastGreenTime() {
            return lastGreenTime;
        }

        public LocalDateTime getLastYellowTime() {
            return lastYellowTime;
        }

        public LocalDateTime getLastRedTime() {
            return lastRedTime;
        }
    }

    public enum FallbackStatus {
        GREEN, YELLOW, RED
    }

    public static class FallbackException extends RuntimeException {

        public FallbackException(String message) {
            super(message);
        }

    }

    /**
     * 降级策略
     */
    @FunctionalInterface
    public interface FallbackStrategy {

        boolean check(FallbackInfo info);

    }

    /**
     * 默认的降级策略
     */
    public static class DefaultFallbackStrategy implements FallbackStrategy {

        private static final Random EXECUTE_RANDOM = new Random();

        private double yellowRatio = 1;
        private double redRatio = 5;

        public DefaultFallbackStrategy() {
        }

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

    @FunctionalInterface
    public interface ErrorProcessor<E> {

        E execute(Throwable e, FallbackHelper.FallbackInfo fallbackInfo);

    }

    @FunctionalInterface
    public interface NormalProcessor<E> {

        E execute() throws Throwable;

    }


}
