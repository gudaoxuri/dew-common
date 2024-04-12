package com.ecfront.dew.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定时器操作.
 *
 * @author gudaoxuri
 */
public class TimerHelper {

    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1);

    private static final Map<String, ScheduledFuture<?>> CONTAINER = new HashMap<>();

    /**
     * Instantiates a new Timer helper.
     */
    TimerHelper() {
    }

    /**
     * 延迟执行的周期性任务.
     *
     * @param delaySec  延迟秒数
     * @param periodSec 周期秒数
     * @param fixedRate 是否按固定周期执行，                  为true时表示每periodSec执行，为false表示fixedDelay，表示在当前任务执行完成后过periodSec再执行
     * @param fun       执行的方法
     * @return 任务id string
     */
    public String periodic(long delaySec, long periodSec, boolean fixedRate, Runnable fun) {
        String id = UUID.randomUUID().toString();
        ScheduledFuture<?> future;
        if (fixedRate) {
            future = EXECUTOR.scheduleAtFixedRate(fun, delaySec, periodSec, TimeUnit.SECONDS);
        } else {
            future = EXECUTOR.scheduleWithFixedDelay(fun, delaySec, periodSec, TimeUnit.SECONDS);
        }
        CONTAINER.put(id, future);
        return id;
    }

    /**
     * 周期性任务.
     *
     * @param periodSec 周期秒数
     * @param fixedRate 是否按固定周期执行，                  为true时表示每periodSec执行，为false表示fixedDelay，表示在当前任务执行完成后过periodSec再执行
     * @param fun       执行的方法
     * @return 任务id string
     */
    public String periodic(long periodSec, boolean fixedRate, Runnable fun) {
        return periodic(0, periodSec, fixedRate, fun);
    }

    /**
     * 取消周期性任务.
     *
     * @param taskId 任务id
     */
    public void cancel(String taskId) {
        if (CONTAINER.containsKey(taskId)) {
            CONTAINER.get(taskId).cancel(true);
            CONTAINER.remove(taskId);
        }
    }

    /**
     * 延迟任务.
     *
     * @param delaySec 延迟秒数
     * @param fun      执行的方法
     */
    public void timer(long delaySec, Runnable fun) {
        EXECUTOR.schedule(fun, delaySec, TimeUnit.SECONDS);
    }

}
