package com.github.swamim.media.organizer.files.copier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.swamim.media.organizer.utils.FileNameUtils.getNameForFileCopy;

public class FileSizeMatchOverwriteCopier extends AbstractCopier {

    @Override
    protected OverwriteOperation overwriteOperation(Path src, Path dest, Path destDir) {
        Path newDest = dest;
        String overwritePrefix = "";
        if (Files.exists(dest)) {
            boolean sameSize = false;
            try {
                sameSize = Files.size(dest) == Files.size(src);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
            if (sameSize) {
                overwritePrefix = "SAME SIZE OVERWRITE ";
            } else {
                newDest = Paths.get(destDir.toString() + File.separator + getNameForFileCopy(src));
                overwritePrefix = "NOT OVERWRITE MAKE COPY ";
            }
        }
        return new OverwriteOperation(true, newDest, overwritePrefix);
    }
}