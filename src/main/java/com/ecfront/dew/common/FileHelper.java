package com.ecfront.dew.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {

    public static String readAll(String path, String encode) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), encode);
    }

}
