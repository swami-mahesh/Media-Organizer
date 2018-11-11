package com.mediaorganizer.organizer.files.copier;

import java.nio.file.Files;
import java.nio.file.Path;

public class NoOverwriteCopier extends AbstractCopier {

    @Override
    protected OverwriteOperation overwriteOperation(Path src, Path dest, Path destDir) {
        Path newDest = dest;
        String overwritePrefix = "";
        if (Files.exists(dest)) {
            return new OverwriteOperation(false, newDest, overwritePrefix);
        }
        return new OverwriteOperation(true, newDest, overwritePrefix);
    }
}