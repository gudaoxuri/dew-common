package com.ecfront.dew.common.tuple;

/**
 * Tuple 3.
 *
 * @param <T0> the type parameter
 * @param <T1> the type parameter
 * @param <T2> the type parameter
 * @author gudaoxuri
 */
public class Tuple3<T0, T1, T2> implements Tuple {

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
     * Instantiates a new Tuple 3.
     */
    public Tuple3() {
    }

    /**
     * Instantiates a new Tuple 3.
     *
     * @param _0 the 0
     * @param _1 the 1
     * @param _2 the 2
     */
    public Tuple3(T0 _0, T1 _1, T2 _2) {
        this._0 = _0;
        this._1 = _1;
        this._2 = _2;
    }

}
