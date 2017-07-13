package com.ecfront.dew.common;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页对象
 */
public class Page<E> implements Serializable {

    /**
     * 当前页，从1开始
     */
    private long pageNumber;
    /**
     * 每页记录数
     */
    private long pageSize;
    /**
     * 总页数
     */
    private long pageTotal;
    /**
     * 总记录数
     */
    private long recordTotal;
    /**
     * 实际对象
     */
    private List<E> objects;

    public static <S> Page<S> build(long pageNumber, long pageSize, long recordTotal, List<S> objects) {
        Page<S> dto = new Page<>();
        dto.pageNumber = pageNumber;
        dto.pageSize = pageSize;
        dto.recordTotal = recordTotal;
        dto.pageTotal = (recordTotal + pageSize - 1) / pageSize;
        dto.objects = objects;
        return dto;
    }

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

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public long getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(long recordTotal) {
        this.recordTotal = recordTotal;
    }

    public List<E> getObjects() {
        return objects;
    }

    public void setObjects(List<E> objects) {
        this.objects = objects;
    }

}
