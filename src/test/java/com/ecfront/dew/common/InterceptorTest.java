package com.ecfront.dew.common;

import com.ecfront.dew.common.interceptor.DewInterceptRespBody;
import com.ecfront.dew.common.interceptor.DewInterceptor;
import com.ecfront.dew.common.interceptor.DewInterceptorProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class InterceptorTest {

    @Test
    public void testInterceptor() throws Exception {
        // none
        Resp<DewInterceptRespBody<Obj>> resp = DewInterceptorProcessor.process("none", new Obj("1"), new HashMap<>(), (obj, context) ->
                Resp.success(DewInterceptRespBody.build(obj, context))
        );
        Assert.assertTrue(resp.ok());
        Assert.assertEquals("1", resp.getBody().getObj().getF());
        // has one
        DewInterceptorProcessor.register("test",new InterceptorA());
        resp = DewInterceptorProcessor.process("test", new Obj("1"), new HashMap<>(), (obj, context) ->
                Resp.success(DewInterceptRespBody.build(obj, context))
        );
        Assert.assertTrue(resp.ok());
        Assert.assertEquals("3", resp.getBody().getObj().getF());
        // error
        DewInterceptorProcessor.register("test",new InterceptorB());
        resp = DewInterceptorProcessor.process("test", new Obj("1"), new HashMap<>(), (obj, context) ->
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
            return Resp.success(DewInterceptRespBody.build(obj,context));
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
            return Resp.success(DewInterceptRespBody.build(obj,context));
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