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
