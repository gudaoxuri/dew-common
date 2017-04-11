package com.ecfront.dew.common.interceptor;

import com.ecfront.dew.common.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拦截器栈执行器
 */
public class DewInterceptorProcessor {

    private static Logger logger = LoggerFactory.getLogger(DewInterceptorProcessor.class);

    private static Map<String, List<DewInterceptor<?>>> CONTAINER = new HashMap<>();

    /**
     * 注册拦截器栈
     *
     * @param category    拦截类型
     * @param interceptor 拦截器
     */
    public static void register(String category, DewInterceptor<?> interceptor) {
        if (!CONTAINER.containsKey(category)) {
            CONTAINER.put(category, new ArrayList<>());
        }
        CONTAINER.get(category).add(interceptor);
    }

    /**
     * 注册拦截器栈
     *
     * @param category     拦截类型
     * @param interceptors 拦截器列表
     */
    public static void register(String category, List<DewInterceptor<?>> interceptors) {
        CONTAINER.put(category, interceptors);
    }

    /**
     * 拦截器栈处理方法
     *
     * @param category 拦截类型
     * @param obj      初始入栈对象
     * @param context  初始入栈参数
     * @param fun      实际执行方法
     * @tparam E 对象的类型
     */
    public static <E> Resp<DewInterceptRespBody<E>> process(String category, E obj, Map<String, Object> context, DewInterceptExec<E> fun) {
        logger.debug("[DewInterceptorProcessor] Process [{}]", category);
        if (!CONTAINER.containsKey(category)) {
            return fun.exec(obj, context);
        }
        List<DewInterceptor<?>> interceptors = CONTAINER.get(category);
        Resp<DewInterceptRespBody<E>> beforeR = doProcess(obj, context, interceptors, true);
        if (!beforeR.ok()) {
            return beforeR;
        }
        Resp<DewInterceptRespBody<E>> execR = fun.exec(beforeR.getBody().getObj(), beforeR.getBody().getContext());
        if (!execR.ok()) {
            return execR;
        }
        return doProcess(obj, context, interceptors, false);
    }

    private static <E> Resp<DewInterceptRespBody<E>> doProcess(E obj, Map<String, Object> context, List<DewInterceptor<?>> interceptors, boolean isBefore) {
        Resp<DewInterceptRespBody<E>> result = Resp.success(DewInterceptRespBody.build(obj, context));
        for (DewInterceptor<?> interceptor : interceptors) {
            logger.trace("[DewInterceptorProcessor] Process interceptor [{}]:{}-{}", interceptor.getCategory(), interceptor.getName(), isBefore ? "before" : "after");
            E preObj = result.getBody().getObj();
            Map<String, Object> preContext = result.getBody().getContext();
            DewInterceptor<E> interceptorE = (DewInterceptor<E>) interceptor;
            try {
                if (isBefore) {
                    result = interceptorE.before(preObj, preContext);
                } else {
                    result = interceptorE.after(preObj, preContext);
                }
                if (!result.ok()) {
                    logger.warn("[DewInterceptorProcessor] Process interceptor error [{}]:{}-{},[{}]{}",
                            interceptor.getCategory(), interceptor.getName(), isBefore ? "before" : "after", result.getCode(), result.getMessage());
                    interceptorE.error(preObj, preContext);
                    return result;
                }
            } catch (Throwable e) {
                result = Resp.serverError(e.getMessage());
                logger.error("[DewInterceptorProcessor] Process interceptor error [{}]:{}-{},[{}]{}",
                        interceptor.getCategory(), interceptor.getName(), isBefore ? "before" : "after", result.getCode(), result.getMessage());
                interceptorE.error(preObj, preContext);
                return result;
            }
        }
        return result;
    }

}
