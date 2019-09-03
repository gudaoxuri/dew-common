/*
 * Copyright 2019. the original author or authors.
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

package com.ecfront.dew.common.interceptor;

import java.util.Map;

/**
 * 操作上下文.
 *
 * @param <I> 输入对象类型
 * @param <O> 输出对象类型
 * @author gudaoxuri
 */
public class DewInterceptContext<I, O> {

    private I input;
    private O output;
    private Map<String, Object> args;

    /**
     * Build dew intercept context.
     *
     * @param <I>    the type parameter
     * @param <O>    the type parameter
     * @param input  the input
     * @param output the output
     * @param args   the args
     * @return the dew intercept context
     */
    public static <I, O> DewInterceptContext<I, O> build(I input, O output, Map<String, Object> args) {
        DewInterceptContext<I, O> context = new DewInterceptContext<>();
        context.setInput(input);
        context.setOutput(output);
        context.setArgs(args);
        return context;
    }

    /**
     * Gets input.
     *
     * @return the input
     */
    public I getInput() {
        return input;
    }

    /**
     * Sets input.
     *
     * @param input the input
     */
    public void setInput(I input) {
        this.input = input;
    }

    /**
     * Gets output.
     *
     * @return the output
     */
    public O getOutput() {
        return output;
    }

    /**
     * Sets output.
     *
     * @param output the output
     */
    public void setOutput(O output) {
        this.output = output;
    }

    /**
     * Gets args.
     *
     * @return the args
     */
    public Map<String, Object> getArgs() {
        return args;
    }

    /**
     * Sets args.
     *
     * @param args the args
     */
    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
