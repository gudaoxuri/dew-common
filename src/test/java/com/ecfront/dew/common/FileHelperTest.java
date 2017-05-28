package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

public class FileHelperTest {

    @Test
    public void testFile() throws Exception {
        String conf = FileHelper.readAllByClassPath("conf1.json", "UTF-8");
        Assert.assertTrue(conf.contains("1"));
        conf=FileHelper.readAllByClassPath("conf/conf2.json","UTF-8");
        Assert.assertTrue(conf.contains("2"));
    }


}