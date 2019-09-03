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

package com.ecfront.dew.common.tuple;

/**
 * Tuple 2.
 *
 * @param <T0> the type parameter
 * @param <T1> the type parameter
 * @author gudaoxuri
 */
public class Tuple2<T0, T1> implements Tuple {

    /**
     * The 0.
     */
    public T0 _0;
    /**
     * The 1.
     */
    public T1 _1;

    /**
     * Instantiates a new Tuple 2.
     */
    public Tuple2() {
    }

    /**
     * Instantiates a new Tuple 2.
     *
     * @param _0 the 0
     * @param _1 the 1
     */
    public Tuple2(T0 _0, T1 _1) {
        this._0 = _0;
        this._1 = _1;
    }
}
