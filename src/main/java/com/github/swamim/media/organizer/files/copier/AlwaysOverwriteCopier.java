package com.github.swamim.media.organizer.files.copier;

import java.nio.file.Files;
import java.nio.file.Path;

public class AlwaysOverwriteCopier extends AbstractCopier {

    @Override
    protected OverwriteOperation overwriteOperation(Path src, Path dest, Path destDir) {
        if (Files.exists(dest)) {
            return new OverwriteOperation(true, dest, "OVERWRITE ");
        }
        else {
            return new OverwriteOperation(true, dest, "");
        }
    }
}
