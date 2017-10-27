package com.ecfront.dew.common;

import java.util.List;
import java.util.Map;

public class GenericModel {

    private List<String> strs;
    private List<Ext> Exts;
    private Map<String, Ext> extMap;

    public List<String> getStrs() {
        return strs;
    }

    public void setStrs(List<String> strs) {
        this.strs = strs;
    }

    public List<Ext> getExts() {
        return Exts;
    }

    public void setExts(List<Ext> exts) {
        Exts = exts;
    }

    public Map<String, Ext> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Ext> extMap) {
        this.extMap = extMap;
    }

}
