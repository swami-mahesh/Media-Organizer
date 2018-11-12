package com.github.swamim.media.organizer.files.copier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.swamim.media.organizer.utils.FileNameUtils.getNameForFileCopy;

public class FileContentMatchOverwriteCopier extends AbstractCopier {

    @Override
    protected OverwriteOperation overwriteOperation(Path src, Path dest, Path destDir) {
        Path newDest = dest;
        String overwritePrefix = "";
        if (Files.exists(dest)) {
            boolean sameContents = false;
            try {
                sameContents = com.google.common.io.Files.equal(src.toFile(), dest.toFile());
            } catch (IOException ioe) {
            }
            if (sameContents) {
                overwritePrefix = "SAME CONTENTS OVERWRITE ";
            } else {
                newDest = Paths.get(destDir.toString() + File.separator + getNameForFileCopy(src));

                overwritePrefix = "NOT OVERWRITE MAKE COPY ";
            }
        }
        return new OverwriteOperation(true, newDest, overwritePrefix);
    }

}