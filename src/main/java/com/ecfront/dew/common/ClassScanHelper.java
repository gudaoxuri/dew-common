/*
 * Copyright 2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecfront.dew.common;


import com.ecfront.dew.common.exception.RTException;
import com.ecfront.dew.common.exception.RTIOException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Java Class扫描操作.
 *
 * @author gudaoxuri
 */
public class ClassScanHelper {

    /**
     * Instantiates a new Class scan helper.
     */
    ClassScanHelper() {
    }

    private static Set<Class<?>> findAndAddClassesByFile(String currentPackage, File currentFile,
                                                         Set<Class<? extends Annotation>> annotations, Set<String> classNames) {
        Set<Class<?>> result = new HashSet<>();
        if (currentFile.exists() && currentFile.isDirectory()) {
            File[] files = currentFile.listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(findAndAddClassesByFile(currentPackage + "." + file.getName(), file, annotations, classNames));
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(currentPackage + '.' + className);
                        if (isMatch(clazz, annotations, classNames)) {
                            result.add(clazz);
                        }
                    } catch (Throwable e) {
                        // Ignore NoClassDefFoundError when class extends/implements some not import class.
                    }
                }
            }
        }
        return result;
    }

    private static Set<Class<?>> findAndAddClassesByJar(JarFile jar, String currentPath,
                                                        Set<Class<? extends Annotation>> annotations, Set<String> classNames) {
        Set<Class<?>> result = new HashSet<>();
        Enumeration<JarEntry> entries = jar.entries();
        JarEntry jarEntry;
        String jarName;
        while (entries.hasMoreElements()) {
            jarEntry = entries.nextElement();
            jarName = jarEntry.getName();
            if (jarName.charAt(0) == '/') {
                jarName = jarName.substring(1);
            }
            if (jarName.startsWith(currentPath)) {
                int idx = jarName.lastIndexOf('/');
                if (jarName.endsWith(".class")
                        && !jarEntry.isDirectory()) {
                    String className = jarName.substring(jarName.lastIndexOf('/') + 1,
                            jarName.length() - 6);
                    try {
                        Class<?> clazz = Class.forName(jarName.substring(0, idx).replace('/', '.') + '.' + className);
                        if (isMatch(clazz, annotations, classNames)) {
                            result.add(clazz);
                        }
                    } catch (Throwable e) {
                        // Ignore NoClassDefFoundError when class extends/implements some not import class.
                    }
                }
            }
        }
        return result;
    }

    private static boolean isMatch(Class<?> clazz, Set<Class<? extends Annotation>> annotations, Set<String> classNames) {
        return matchAnnotation(clazz, annotations) && matchClassName(clazz, classNames);
    }

    private static boolean matchAnnotation(Class<?> clazz, Set<Class<? extends Annotation>> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return true;
        }
        for (Class<? extends Annotation> annotation : annotations) {
            if (clazz.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchClassName(Class<?> clazz, Set<String> classNames) {
        if (classNames == null || classNames.isEmpty()) {
            return true;
        }
        for (String className : classNames) {
            if (Pattern.compile(className)
                    .matcher(clazz.getSimpleName()).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扫描获取指定包下符合条件的class类.
     * <p>
     * IMPORTANT: 此方法不支持 Native Image https://github.com/oracle/graal/issues/1108
     *
     * @param basePackage        要扫描的根包名
     * @param includeAnnotations 要包含的注解，默认为全部
     * @param includeNames       要包含的class名称（支持正则），默认为全部
     * @return 符合条件的class类集合 set
     */
    public Set<Class<?>> scan(String basePackage,
                              Set<Class<? extends Annotation>> includeAnnotations, Set<String> includeNames) throws RTIOException {
        Set<Class<?>> result = new HashSet<>();
        String packageDir = basePackage.replace('.', '/');
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageDir);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                switch (url.getProtocol()) {
                    case "file":
                        result.addAll(
                                findAndAddClassesByFile(
                                        basePackage, new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8)),
                                        includeAnnotations, includeNames)
                        );
                        break;
                    case "jar":
                        result.addAll(
                                findAndAddClassesByJar(
                                        ((JarURLConnection) url.openConnection()).getJarFile(),
                                        packageDir, includeAnnotations, includeNames)
                        );
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new RTException(e);
        }
        return result;
    }

}
