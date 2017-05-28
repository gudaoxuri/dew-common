package com.ecfront.dew.common;

import com.ecfront.dew.common.interceptor.DewInterceptRespBody;
import com.ecfront.dew.common.interceptor.DewInterceptor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class InterceptorTest {

    @Test
    public void testInterceptor() throws Exception {
        // 没有注册拦截器的情况
        Resp<DewInterceptRespBody<Obj>> resp = DEW.interceptor.process("none", new Obj("1"), new HashMap<>(), (obj, context) ->
                Resp.success(DewInterceptRespBody.build(obj, context))
        );
        Assert.assertTrue(resp.ok());
        Assert.assertEquals("1", resp.getBody().getObj().getF());
        // 注册了一个拦截器A
        DEW.interceptor.register("test", new InterceptorA());
        resp = DEW.interceptor.process("test", new Obj("1"), new HashMap<>(), (obj, context) ->
                Resp.success(DewInterceptRespBody.build(obj, context))
        );
        Assert.assertTrue(resp.ok());
        Assert.assertEquals("3", resp.getBody().getObj().getF());
        // 注册了另一个拦截器B，假设B执行会报错
        DEW.interceptor.register("test", new InterceptorB());
        resp = DEW.interceptor.process("test", new Obj("1"), new HashMap<>(), (obj, context) ->
                Resp.success(DewInterceptRespBody.build(obj, context))
        );
        Assert.assertTrue(!resp.ok());
    }


    public class Obj {
        private String f;

        public Obj(String f) {
            this.f = f;
        }

        public String getF() {
            return f;
        }

        public void setF(String f) {
            this.f = f;
        }
    }

    public class InterceptorA implements DewInterceptor<Obj> {

        @Override
        public String getCategory() {
            return "test";
        }

        @Override
        public String getName() {
            return "A";
        }

        @Override
        public Resp<DewInterceptRespBody<Obj>> after(Obj obj, Map context) {
            obj.setF("3");
            return Resp.success(DewInterceptRespBody.build(obj, context));
        }

        @Override
        public Resp<DewInterceptRespBody<Obj>> before(Obj obj, Map context) {
            obj.setF("2");
            return Resp.success(DewInterceptRespBody.build(obj, context));
        }
    }

    public class InterceptorB implements DewInterceptor<Obj> {

        @Override
        public String getCategory() {
            return "test";
        }

        @Override
        public String getName() {
            return "B";
        }

        @Override
        public Resp<DewInterceptRespBody<Obj>> after(Obj obj, Map context) {
            return Resp.success(DewInterceptRespBody.build(obj, context));
        }

        @Override
        public Resp<DewInterceptRespBody<Obj>> before(Obj obj, Map context) {
            return Resp.badRequest("some error");
        }

        @Override
        public void error(Obj obj, Map<String, Object> context) {
            obj.setF("error");
        }
    }


}