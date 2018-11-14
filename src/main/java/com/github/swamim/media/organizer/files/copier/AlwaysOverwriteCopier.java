package com.github.swamim.media.organizer.files.copier;

import java.nio.file.Files;
import java.nio.file.Path;

public class AlwaysOverwriteCopier extends AbstractCopier {

    @Override
    protected OverwriteDecision overwriteDecision(Path src, Path dest, Path destDir) {
        if (Files.exists(dest)) {
            return new OverwriteDecision().to(dest).withLogPrefix("OVERWRITE ").proceed();
        }
        else {
            return new OverwriteDecision().to(dest).proceed();
        }
    }
}
