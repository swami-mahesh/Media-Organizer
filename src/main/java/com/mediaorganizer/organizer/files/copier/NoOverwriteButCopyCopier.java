package com.mediaorganizer.organizer.files.copier;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.mediaorganizer.organizer.utils.FileNameUtils.getNameForFileCopy;

public class NoOverwriteButCopyCopier extends AbstractCopier {

    @Override
    protected OverwriteOperation overwriteOperation(Path src, Path dest, Path destDir) {
        Path newDest = dest;
        String overwritePrefix = "";
        if (Files.exists(dest)) {
            newDest = Paths.get(destDir.toString() + File.separator + getNameForFileCopy(src));
            overwritePrefix = "NOT OVERWRITE MAKE COPY ";
        }
        return new OverwriteOperation(true, newDest, overwritePrefix);
    }

}