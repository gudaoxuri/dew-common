package com.ecfront.dew.commonit.test;

import com.ecfront.dew.common.test.graalvm.NativeImageMain;
import org.junit.jupiter.api.Test;

/**
 * The type Native image test.
 *
 * @author gudaoxuri
 */
public class NativeImageTest {

    /**
     * Test native image.
     *
     * @throws Exception the exception
     */
    @Test
    public void testNativeImage() throws Exception {
        if (System.getProperty("java.vm.vendor").contains("GraalVM")) {
            NativeImageMain.main(new String[]{});
        }
    }

}
