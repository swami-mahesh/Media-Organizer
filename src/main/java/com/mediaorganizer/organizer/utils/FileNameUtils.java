package com.mediaorganizer.organizer.utils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class FileNameUtils {

    private FileNameUtils() {
    }

    public static String getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse("");
    }

    public static String getFileNameWithoutExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(0, filename.lastIndexOf("."))).orElse("");
    }


    public static String getNameForFileCopy(Path src) {
        return getFileNameWithoutExtension(src.getFileName().toString()) + "_COPY_" + UUID.randomUUID().toString() + "."+
                getExtension(src.getFileName().toString());
    }
}
