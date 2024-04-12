package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTIOException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 常用文件操作.
 *
 * @author gudaoxuri
 */
public class FileHelper {

    /**
     * Instantiates a new File helper.
     */
    FileHelper() {
    }

    /**
     * 根据文件路径名读取文件所有内容.
     *
     * @param pathName 文件路径名
     * @param encode   编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByPathName(String pathName, String encode) throws RTIOException {
        return readAllByPathName(pathName, Charset.forName(encode));
    }

    /**
     * 根据文件路径名读取文件所有内容.
     *
     * @param pathName 文件路径名
     * @param encode   编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByPathName(String pathName, Charset encode) throws RTIOException {
        try {
            return Files.readString(Paths.get(pathName), encode);
        } catch (IOException e) {
            throw new RTIOException(e);
        }
    }

    /**
     * 根据文件读取文件所有内容.
     *
     * @param file   文件
     * @param encode 编码
     * @return 文件内容 string
     * @throws RTIOException the rtio exception
     */
    public String readAllByFile(File file, String encode) throws RTIOException {
        return readAllByFile(file, Charset.forName(encode));
    }

    /**
     * 根据文件读取文件所有内容.
     *
     * @param file   文件
     * @param encode 编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByFile(File file, Charset encode) throws RTIOException {
        try {
            return Files.readString(file.toPath(), encode);
        } catch (IOException e) {
            throw new RTIOException(e);
        }
    }

    /**
     * 根据文件路径读取文件所有内容.
     *
     * @param path   文件路径
     * @param encode 编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByPath(Path path, String encode) throws RTIOException {
        return readAllByPath(path, Charset.forName(encode));
    }

    /**
     * 根据文件路径读取文件所有内容.
     *
     * @param path   文件路径
     * @param encode 编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByPath(Path path, Charset encode) throws RTIOException {
        try {
            return Files.readString(path, encode);
        } catch (IOException e) {
            throw new RTIOException(e);
        }
    }

    /**
     * 根据classpath读取文件所有内容（jar包外路径优先）.
     * <p>
     *
     * @param classpath classpath，先找jar外的文件，找不到再去读jar包内文件
     * @param encode    编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByClassPath(String classpath, String encode) throws RTIOException {
        return readAllByClassPath(classpath, Charset.forName(encode));
    }

    /**
     * 根据classpath读取文件所有内容（jar包外路径优先）.
     * <p>
     *
     * @param classpath classpath，先找jar外的文件，找不到再去读jar包内文件
     * @param encode    编码
     * @return 文件内容
     * @throws RTIOException the rtio exception
     */
    public String readAllByClassPath(String classpath, Charset encode) throws RTIOException {
        var path = ClassLoader.getSystemResource("");
        if (path != null) {
            File file = new File(path + classpath);
            if (file.exists()) {
                return readAllByFile(file, encode);
            }
        }
        File file = new File(FileHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (file.isFile()) {
            file = file.getParentFile();
            file = new File(file.getPath() + File.separator + classpath);
            if (file.exists()) {
                return readAllByFile(file, encode);
            }
        }
        InputStream in = null;
        BufferedReader buffer = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath);
            if (in == null) {
                in = this.getClass().getResourceAsStream(classpath);
            }
            if (in == null) {
                return null;
            }
            buffer = new BufferedReader(new InputStreamReader(in));
            return buffer.lines().collect(Collectors.joining("\n"));
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RTIOException(e);
            }
        }
    }

    /**
     * 从流中复制文件到磁盘，不支持目录.
     *
     * @param source   流，支持jar内文件复制
     *                 e.g. Test.class.getResourceAsStream("/LICENSE-junit.txt")
     * @param destPath 磁盘路径
     * @throws RTIOException the rtio exception
     */
    public void copyStreamToPath(InputStream source, String destPath) throws RTIOException {
        try {
            Files.copy(source, Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
            source.close();
        } catch (IOException e) {
            throw new RTIOException(e);
        }
    }

    /**
     * 判断是否是Windows系统.
     *
     * @return 是否是Windows系统 boolean
     */
    public boolean isWindows() {
        return System.getProperty("os.name", "linux").toLowerCase().startsWith("windows");
    }


    /**
     * Glob模式文件过滤器.
     *
     * @param files     要过滤的文件列表
     * @param mathRules Glob过滤规则列表
     * @return 过滤后的文件列表 list
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob">Glob过滤规则</a>
     */
    public List<String> mathFilter(List<String> files, List<String> mathRules) {
        if (mathRules.isEmpty()) {
            return files;
        }
        List<PathMatcher> matchers = convertGlobMatchers(mathRules);
        return convertPaths(files)
                .filter(path -> matchers.stream().anyMatch(mather -> mather.matches(path)))
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    /**
     * 使用Glob模式过滤规则检查是否有匹配到的文件.
     *
     * @param files     要匹配的文件列表
     * @param mathRules Glob过滤规则列表
     * @return 是否有匹配到的文件 boolean
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob">Glob过滤规则</a>
     */
    public boolean anyMath(List<String> files, List<String> mathRules) {
        if (files.isEmpty()) {
            return false;
        }
        if (mathRules.isEmpty()) {
            return true;
        }
        List<PathMatcher> matchers = convertGlobMatchers(mathRules);
        return convertPaths(files).anyMatch(path -> matchers.stream().anyMatch(mather -> mather.matches(path)));
    }

    /**
     * 使用Glob模式过滤规则检查是否有未匹配到的文件.
     *
     * @param files     要匹配的文件列表
     * @param mathRules Glob过滤规则列表
     * @return 是否有未匹配到的文件 boolean
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob">Glob过滤规则</a>
     */
    public boolean noneMath(List<String> files, List<String> mathRules) {
        if (files.isEmpty()) {
            return true;
        }
        if (mathRules.isEmpty()) {
            return false;
        }
        List<PathMatcher> matchers = convertGlobMatchers(mathRules);
        return convertPaths(files).anyMatch(path -> matchers.stream().noneMatch(mather -> mather.matches(path)));
    }

    private List<PathMatcher> convertGlobMatchers(List<String> mathRules) {
        return mathRules.stream().map(rule -> FileSystems.getDefault().getPathMatcher("glob:" + rule)).collect(Collectors.toList());
    }

    private Stream<Path> convertPaths(List<String> files) {
        return files.stream().map(Paths::get);
    }

}
