package com.github.swamim.media.organizer.files.copier;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.swamim.media.organizer.utils.FileNameUtils.getNameForFileCopy;

public class NoOverwriteButCopyCopier extends AbstractCopier {

    @Override
    protected OverwriteDecision overwriteDecision(Path src, Path dest, Path destDir) {
        Path newDest = dest;
        String overwritePrefix = "";
        if (Files.exists(dest)) {
            newDest = Paths.get(destDir.toString() + File.separator + getNameForFileCopy(src));
            overwritePrefix = "NOT OVERWRITE MAKE COPY ";
        }
        return new OverwriteDecision().to(newDest).withLogPrefix(overwritePrefix).proceed();
    }

}