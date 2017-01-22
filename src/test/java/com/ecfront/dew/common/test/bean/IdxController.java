package com.ecfront.dew.common.test.bean;

import java.util.List;
import java.util.Map;

@TestAnnotation.RPC(path = "/idx/")
public class IdxController extends BaseController {

    @Deprecated
    private String childField;

    @TestAnnotation.POST(path = "")
    public List<String> childFind(Map<String, String> args, List<String> body) {
        body.add("new");
        return body;
    }

    @Override
    public User find(Map<String, String> args, User body) {
        return super.find(args, body);
    }
}
