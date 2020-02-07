/*
 * Copyright 2020. the original author or authors.
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

package com.ecfront.dew.common;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页对象.
 *
 * @param <E> the type parameter
 * @author gudaoxuri
 */
public class Page<E> implements Serializable {

    /**
     * 当前页，从1开始.
     */
    private long pageNumber;
    /**
     * 每页记录数.
     */
    private long pageSize;
    /**
     * 总页数.
     */
    private long pageTotal;
    /**
     * 总记录数.
     */
    private long recordTotal;
    /**
     * 实际对象.
     */
    private List<E> objects;

    /**
     * Build page.
     *
     * @param <S>         the type parameter
     * @param pageNumber  the page number
     * @param pageSize    the page size
     * @param recordTotal the record total
     * @param objects     the objects
     * @return the page
     */
    public static <S> Page<S> build(long pageNumber, long pageSize, long recordTotal, List<S> objects) {
        Page<S> dto = new Page<>();
        dto.pageNumber = pageNumber;
        dto.pageSize = pageSize;
        dto.recordTotal = recordTotal;
        dto.pageTotal = (recordTotal + pageSize - 1) / pageSize;
        dto.objects = objects;
        return dto;
    }

    /**
     * Convert page.
     *
     * @param <T>       the type parameter
     * @param converter the converter
     * @return the page
     */
    public <T> Page<T> convert(Function<E, T> converter) {
        Page<T> page = new Page<>();
        page.setPageNumber(this.getPageNumber());
        page.setPageSize(this.getPageSize());
        page.setPageTotal(this.getPageTotal());
        page.setRecordTotal(this.getRecordTotal());
        if (this.getObjects() != null) {
            page.setObjects(this.getObjects().stream()
                    .map(converter)
                    .collect(Collectors.toList()));
        } else {
            page.setObjects(null);
        }
        return page;
    }

    /**
     * Is first page.
     *
     * @return the boolean
     */
    public boolean isFirstPage() {
        return pageNumber == 1;
    }

    /**
     * Is last page.
     *
     * @return the boolean
     */
    public boolean isLastPage() {
        return pageNumber == pageTotal;
    }

    /**
     * Has next page.
     *
     * @return the boolean
     */
    public boolean hasNextPage() {
        return pageNumber < pageTotal;
    }

    /**
     * Gets page number.
     *
     * @return the page number
     */
    public long getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets page number.
     *
     * @param pageNumber the page number
     */
    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets page total.
     *
     * @return the page total
     */
    public long getPageTotal() {
        return pageTotal;
    }

    /**
     * Sets page total.
     *
     * @param pageTotal the page total
     */
    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }

    /**
     * Gets record total.
     *
     * @return the record total
     */
    public long getRecordTotal() {
        return recordTotal;
    }

    /**
     * Sets record total.
     *
     * @param recordTotal the record total
     */
    public void setRecordTotal(long recordTotal) {
        this.recordTotal = recordTotal;
    }

    /**
     * Gets objects.
     *
     * @return the objects
     */
    public List<E> getObjects() {
        return objects;
    }

    /**
     * Sets objects.
     *
     * @param objects the objects
     */
    public void setObjects(List<E> objects) {
        this.objects = objects;
    }

}
