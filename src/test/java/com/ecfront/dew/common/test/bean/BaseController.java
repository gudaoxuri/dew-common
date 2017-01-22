package com.ecfront.dew.common.test.bean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class BaseController {

    @Resource
    private String parentField;

    public String getParentField() {
        return parentField;
    }

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    @TestAnnotation.POST(path = "")
    public  List<String> parentFind(Map<String, String> args, List<String> body) {
        body.add("parent");
        return body;
    }

    @TestAnnotation.POST(path = "user/")
    public  User find(Map<String, String> args, User body) {
        return body;
    }

    @TestAnnotation.GET(path = "user/")
    public  User get(Map<String, String> args) {
        return null;
    }

}
