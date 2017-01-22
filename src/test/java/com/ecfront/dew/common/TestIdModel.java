package com.ecfront.dew.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TestIdModel extends Ext {
    private String name;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
