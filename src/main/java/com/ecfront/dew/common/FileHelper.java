package com.ecfront.dew.common;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * 常用文件操作
 */
public class FileHelper {

    /**
     * 根据文件路径名读取文件所有内容
     *
     * @param pathName 文件路径名
     * @param encode   编码
     * @return 文件内容
     */
    public String readAllByPathName(String pathName, String encode) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathName)), encode);
    }

    /**
     * 根据文件读取文件所有内容
     *
     * @param file   文件
     * @param encode 编码
     * @return 文件内容
     */
    public String readAllByFile(File file, String encode) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), encode);
    }

    /**
     * 根据文件路径读取文件所有内容
     *
     * @param path   文件路径
     * @param encode 编码
     * @return 文件内容
     */
    public String readAllByPath(Path path, String encode) throws IOException {
        return new String(Files.readAllBytes(path), encode);
    }

    /**
     * 根据classpath读取文件所有内容（jar包外路径优先）
     *
     * @param classpath classpath，先找jar外的文件，找不到再去读jar包内文件
     * @param encode    编码
     * @return 文件内容
     * @throws IOException
     */
    public String readAllByClassPath(String classpath, String encode) throws IOException {
        File file = new File(FileHelper.class.getResource("/").getPath() + classpath);
        if (file.exists()) {
            return readAllByFile(file, encode);
        }
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        return buffer.lines().collect(Collectors.joining("\n"));
    }


}
