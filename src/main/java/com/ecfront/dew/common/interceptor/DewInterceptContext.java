package com.ecfront.dew.common.interceptor;

import java.util.Map;

/**
 * 操作上下文
 *
 * @param <I> 输入对象类型
 * @param <O> 输出对象类型
 */
public class DewInterceptContext<I, O> {

    private I input;
    private O output;
    private Map<String, Object> args;

    public static <I, O> DewInterceptContext<I, O> build(I input, O output, Map<String, Object> args) {
        DewInterceptContext<I, O> context = new DewInterceptContext<>();
        context.setInput(input);
        context.setOutput(output);
        context.setArgs(args);
        return context;
    }

    public I getInput() {
        return input;
    }

    public void setInput(I input) {
        this.input = input;
    }

    public O getOutput() {
        return output;
    }

    public void setOutput(O output) {
        this.output = output;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
