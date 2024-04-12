package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type File helper test.
 *
 * @author gudaoxuri
 */
public class FileHelperTest {

    private String currentPath;

    {
        currentPath = FileHelperTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(currentPath);
        currentPath = file.getPath();
        if (file.isFile()) {
            currentPath = file.getParentFile().getPath();
        }
        System.out.println("Current Path:" + currentPath);
    }

    /**
     * Test file.
     */
    @Test
    public void testFile() {
        String conf = $.file.readAllByClassPath("conf1.json", "UTF-8");
        assertTrue(conf.contains("1"));
        conf = $.file.readAllByClassPath("conf/conf2.json", "UTF-8");
        assertTrue(conf.contains("2"));

        $.file.copyStreamToPath(Test.class.getResourceAsStream("/META-INF/LICENSE.md"), currentPath + File.separator + "LICENSE-junit-copy" +
                ".txt");
        assertTrue(new File(currentPath + File.separator + "LICENSE-junit-copy.txt").exists());
        new File(currentPath + File.separator + "LICENSE-junit-copy.txt").delete();
        // Glob Math
        List<String> files = new ArrayList<>() {
            {
                add("/models/parent/pom.xml");
                add("/models/kernel/pom.xml");
                add("/models/kernel/src/main/java/com/ecfront/test/Test.java");
                add("/models/kernel/src/main/java/com/ecfront/test/Main.java");
                add("/models/kernel/src/main/resource/bootstrap.yml");
                add("/models/kernel/src/main/resource/application.yml");
                add("/models/pom.xml");
                add("/models/README.adoc");
                add("/models/.gitignore");
            }
        };
        List<String> matchFiles = $.file.mathFilter(files, new ArrayList<>());
        assertEquals(files.size(), matchFiles.size());
        matchFiles = $.file.mathFilter(files, new ArrayList<>() {
            {
                add("/models/*.xml");
            }
        });
        assertEquals(1, matchFiles.size());
        matchFiles = $.file.mathFilter(files, new ArrayList<>() {
            {
                add("**/*.xml");
            }
        });
        assertEquals(3, matchFiles.size());
        matchFiles = $.file.mathFilter(files, new ArrayList<>() {
            {
                add("**/*.xml");
                add("**/*.{java,yml}");
            }
        });
        assertEquals(7, matchFiles.size());
        matchFiles = $.file.mathFilter(files, new ArrayList<>() {
            {
                add("**/java/**");
            }
        });
        assertEquals(2, matchFiles.size());
        assertTrue($.file.anyMath(files, new ArrayList<>() {
            {
                add("**/java/**");
            }
        }));
        assertTrue($.file.noneMath(files, new ArrayList<>() {
            {
                add("**/java/*");
            }
        }));


    }

}
