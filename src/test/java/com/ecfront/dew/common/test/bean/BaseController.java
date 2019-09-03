package com.ecfront.dew.common.test.bean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * The type Base controller.
 *
 * @author gudaoxuri
 */
public class BaseController {

    @Resource
    private String parentField;

    /**
     * Gets parent field.
     *
     * @return the parent field
     */
    public String getParentField() {
        return parentField;
    }

    /**
     * Sets parent field.
     *
     * @param parentField the parent field
     */
    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    /**
     * Parent find list.
     *
     * @param args the args
     * @param body the body
     * @return the list
     */
    @TestAnnotation.POST(path = "")
    public List<String> parentFind(Map<String, String> args, List<String> body) {
        body.add("parent");
        return body;
    }

    /**
     * Find user.
     *
     * @param args the args
     * @param body the body
     * @return the user
     */
    @TestAnnotation.POST(path = "user/")
    public User find(Map<String, String> args, User body) {
        return body;
    }

    /**
     * Get user.
     *
     * @param args the args
     * @return the user
     */
    @TestAnnotation.GET(path = "user/")
    public User get(Map<String, String> args) {
        return null;
    }

}
