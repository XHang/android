package com.example.util;

public class fileUtil {

    public static String getFileName(String path) throws Exception {
        int index = path.lastIndexOf("/");
        if (index < 0) {
            throw new Exception(String.format("path  %s is illegal", path));
        }
        String name = path.substring(index, path.length());
        index = name.indexOf(".");
        if (index < 0) {
            return name;
        }
        return name.substring(0, index);

    }
}
