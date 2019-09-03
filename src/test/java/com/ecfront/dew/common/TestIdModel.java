package com.ecfront.dew.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * The type Test id model.
 *
 * @author gudaoxuri
 */
public class TestIdModel extends Ext {
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private LocalDateTime localDateTime;
    private LocalDate localDate;
    private LocalTime localTime;

    private Optional<Map<String, Object>> opt;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets local date time.
     *
     * @return the local date time
     */
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Sets local date time.
     *
     * @param localDateTime the local date time
     */
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    /**
     * Gets local date.
     *
     * @return the local date
     */
    public LocalDate getLocalDate() {
        return localDate;
    }

    /**
     * Sets local date.
     *
     * @param localDate the local date
     */
    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    /**
     * Gets local time.
     *
     * @return the local time
     */
    public LocalTime getLocalTime() {
        return localTime;
    }

    /**
     * Sets local time.
     *
     * @param localTime the local time
     */
    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    /**
     * Gets opt.
     *
     * @return the opt
     */
    public Optional<Map<String, Object>> getOpt() {
        return opt;
    }

    /**
     * Sets opt.
     *
     * @param opt the opt
     */
    public void setOpt(Optional<Map<String, Object>> opt) {
        this.opt = opt;
    }
}
