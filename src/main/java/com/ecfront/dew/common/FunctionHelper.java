package com.ecfront.dew.common;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 函数操作 TBD
 */
public class FunctionHelper {

    FunctionHelper() {
    }

    public <E> Stream<E> stream(Iterator<E> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

}
