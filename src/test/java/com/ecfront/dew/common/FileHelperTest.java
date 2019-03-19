package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FileHelperTest {

    @Test
    public void testFile() throws Exception {
        String conf = $.file.readAllByClassPath("conf1.json", "UTF-8");
        Assert.assertTrue(conf.contains("1"));
        conf = $.file.readAllByClassPath("conf/conf2.json", "UTF-8");
        Assert.assertTrue(conf.contains("2"));

        // Glob Math
        List<String> files = new ArrayList<String>(){{
            add("/models/parent/pom.xml");
            add("/models/kernel/pom.xml");
            add("/models/kernel/src/main/java/com/ecfront/test/Test.java");
            add("/models/kernel/src/main/java/com/ecfront/test/Main.java");
            add("/models/kernel/src/main/resource/bootstrap.yml");
            add("/models/kernel/src/main/resource/application.yml");
            add("/models/pom.xml");
            add("/models/README.adoc");
            add("/models/.gitignore");
        }};
        List<String> matchFiles = $.file.mathFilter(files,new ArrayList<String>(){{}});
        Assert.assertEquals(files.size(),matchFiles.size());
        matchFiles = $.file.mathFilter(files,new ArrayList<String>(){{
            add("/models/*.xml");
        }});
        Assert.assertEquals(1,matchFiles.size());
        matchFiles = $.file.mathFilter(files,new ArrayList<String>(){{
            add("**/*.xml");
        }});
        Assert.assertEquals(3,matchFiles.size());
        matchFiles = $.file.mathFilter(files,new ArrayList<String>(){{
            add("**/*.xml");
            add("**/*.{java,yml}");
        }});
        Assert.assertEquals(7,matchFiles.size());
        matchFiles = $.file.mathFilter(files,new ArrayList<String>(){{
            add("**/java/**");
        }});
        Assert.assertEquals(2,matchFiles.size());
        Assert.assertTrue($.file.anyMath(files,new ArrayList<String>(){{
            add("**/java/**");
        }}));
        Assert.assertTrue($.file.noneMath(files,new ArrayList<String>(){{
            add("**/java/*");
        }}));
    }

}