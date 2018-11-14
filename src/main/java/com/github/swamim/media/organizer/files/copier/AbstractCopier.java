package com.github.swamim.media.organizer.files.copier;

import com.github.swamim.media.organizer.files.CopyMode;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Consumer;

public abstract class AbstractCopier {

    private static final Logger logger = Logger.getLogger(AbstractCopier.class);

    protected abstract OverwriteDecision overwriteDecision(Path src, Path dest, Path destDir);

    public void copyFiles(Path source, Collection<String> toFolders, String subFolderName, CopyMode copyMode) {
        toFolders.stream().map(Paths::get).forEach(processDestinationFolder(source, subFolderName, copyMode));
    }

    private Consumer<Path> processDestinationFolder(Path src, String subFolderName, CopyMode copyMode) {
        return destinationBaseDir -> {
            Path destDir = Paths.get(destinationBaseDir.toString() + File.separator + subFolderName);
            Path dest = Paths.get(destDir.toString() + File.separator + src.getFileName());

            OverwriteDecision overwriteDecision = overwriteDecision(src, dest, destDir);
            if (!overwriteDecision.shouldProceed) {
                logger.warn("For original file " + src + " the to file already exists at " + dest + " skipping copy.");
                return;
            }
            dest = overwriteDecision.destination;
            performCopy(src, copyMode, destDir, dest, overwriteDecision);
        };
    }

    private void performCopy(Path src, CopyMode copyMode, Path destDir, Path dest, OverwriteDecision overwriteDecision) {
        if (!copyMode.isDryRun()) {
            if (!createNecessaryDirectories(destDir)) {
                return;
            }
        }
        if (copyMode == CopyMode.MOVE || copyMode == CopyMode.MOVE_DRY_RUN) {
            move(src, copyMode.isDryRun(), dest, overwriteDecision);
        } else {
            copy(src, copyMode.isDryRun(), dest, overwriteDecision);
        }
    }

    private boolean createNecessaryDirectories(Path destDir) {
        try {
            Files.createDirectories(destDir);
        } catch (IOException e) {
            logger.error("Error occurred while creating directory " + destDir, e);
            return false;
        }
        return true;
    }

    private void copy(Path path, boolean isDryRun, Path dest, OverwriteDecision overwriteDecision) {
        logger.info(overwriteDecision.logPrefix + "Copying " + path + " to " + dest);
        if (!isDryRun) {
            try {
                Files.copy(path, dest);
            } catch (IOException e) {
                logger.error("Error occurred while moving file " + path + " to " + dest, e);
            }
        }
    }

    private void move(Path path, boolean isDryRun, Path dest, OverwriteDecision overwriteDecision) {
        logger.info(overwriteDecision.logPrefix + "Moving " + path + " to " + dest);
        if (!isDryRun) {
            try {
                Files.move(path, dest);
            } catch (IOException e) {
                logger.error("Error occurred while moving file " + path + " to " + dest, e);
            }
        }
    }


    protected static class OverwriteDecision {
        private boolean shouldProceed = false;
        private Path destination;
        private String logPrefix = "";

        protected OverwriteDecision proceed() {
            this.shouldProceed = true;
            return this;
        }

        protected OverwriteDecision doNotProceed() {
            this.shouldProceed = false;
            return this;
        }

        protected OverwriteDecision to(Path destination) {
            this.destination = destination;
            return this;
        }

        protected OverwriteDecision withLogPrefix(String logPrefix) {
            this.logPrefix = logPrefix;
            return this;
        }

    }
}

