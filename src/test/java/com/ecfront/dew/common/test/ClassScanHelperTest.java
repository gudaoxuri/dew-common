package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Class scan helper test.
 *
 * @author gudaoxuri
 */
public class ClassScanHelperTest {

    /**
     * Scan.
     */
    @Test
    public void scan() {
        Set<Class<?>> resultInFile = $.clazz.scan("com.ecfront.dew.common.test", new HashSet<>() {
            {
                add(Deprecated.class);
            }
        }, null);
        Assertions.assertEquals(2, resultInFile.size());
        Set<Class<?>> resultInJar = $.clazz.scan("org.junit", null, new HashSet<>() {
            {
                add("Before\\w*");
            }
        });
        Assertions.assertTrue(resultInJar.size() > 0);
    }

}
