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
