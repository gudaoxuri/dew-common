package com.ecfront.dew.common.test;

import java.util.List;
import java.util.Map;

/**
 * The type Generic model.
 *
 * @author gudaoxuri
 */
public class GenericModel {

    private List<String> strs;
    private List<Ext> Exts;
    private Map<String, Ext> extMap;

    /**
     * Gets strs.
     *
     * @return the strs
     */
    public List<String> getStrs() {
        return strs;
    }

    /**
     * Sets strs.
     *
     * @param strs the strs
     */
    public void setStrs(List<String> strs) {
        this.strs = strs;
    }

    /**
     * Gets exts.
     *
     * @return the exts
     */
    public List<Ext> getExts() {
        return Exts;
    }

    /**
     * Sets exts.
     *
     * @param exts the exts
     */
    public void setExts(List<Ext> exts) {
        Exts = exts;
    }

    /**
     * Gets ext map.
     *
     * @return the ext map
     */
    public Map<String, Ext> getExtMap() {
        return extMap;
    }

    /**
     * Sets ext map.
     *
     * @param extMap the ext map
     */
    public void setExtMap(Map<String, Ext> extMap) {
        this.extMap = extMap;
    }

}
