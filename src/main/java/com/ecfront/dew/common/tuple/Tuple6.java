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
 * Tuple 6.
 *
 * @param <T0> the type parameter
 * @param <T1> the type parameter
 * @param <T2> the type parameter
 * @param <T3> the type parameter
 * @param <T4> the type parameter
 * @param <T5> the type parameter
 * @author gudaoxuri
 */
public class Tuple6<T0, T1, T2, T3, T4, T5> implements Tuple {


    /**
     * The 0.
     */
    public T0 _0;
    /**
     * The 1.
     */
    public T1 _1;
    /**
     * The 2.
     */
    public T2 _2;
    /**
     * The 3.
     */
    public T3 _3;
    /**
     * The 4.
     */
    public T4 _4;
    /**
     * The 5.
     */
    public T5 _5;


    /**
     * Instantiates a new Tuple 6.
     */
    public Tuple6() {
    }

    /**
     * Instantiates a new Tuple 6.
     *
     * @param _0 the 0
     * @param _1 the 1
     * @param _2 the 2
     * @param _3 the 3
     * @param _4 the 4
     * @param _5 the 5
     */
    public Tuple6(T0 _0, T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
        this._0 = _0;
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
    }
}
