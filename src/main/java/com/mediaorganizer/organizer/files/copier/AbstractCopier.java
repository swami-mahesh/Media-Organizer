package com.mediaorganizer.organizer.files.copier;

import com.mediaorganizer.organizer.files.CopyMode;
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

    protected abstract OverwriteOperation overwriteOperation(Path src, Path dest, Path destDir);

    public void copyFiles(Path src, Collection<String> toFolders, String subFolderName, CopyMode copyMode) {
        toFolders.stream().map(Paths::get).forEach(processDestinationFolder(src, subFolderName, copyMode));
    }

    private Consumer<Path> processDestinationFolder(Path src, String subFolderName, CopyMode copyMode) {
        return destinationBaseDir -> {
            Path destDir = Paths.get(destinationBaseDir.toString() + File.separator + subFolderName);
            Path dest = Paths.get(destDir.toString() + File.separator + src.getFileName());

            OverwriteOperation overwriteOperation = overwriteOperation(src, dest, destDir);
            if (!overwriteOperation.shouldProceed) {
                logger.warn("For original file " + src + " the destination file already exists at " + dest + " skipping copy.");
                return;
            }
            dest = overwriteOperation.destination;
            boolean isDryRun = (copyMode == CopyMode.COPY_DRY_RUN) || (copyMode == CopyMode.MOVE_DRY_RUN);
            if(!isDryRun) {
                if (!createNecessaryDirectories(destDir)) {
                    return;
                }
            }
            if (copyMode == CopyMode.MOVE || copyMode == CopyMode.MOVE_DRY_RUN) {
                move(src, isDryRun, dest, overwriteOperation);
            } else {
                copy(src, isDryRun, dest, overwriteOperation);
            }
        };
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

    private void copy(Path path, boolean isDryRun, Path dest, OverwriteOperation overwriteOperation) {
        logger.info(overwriteOperation.logPrefix + "Copying " + path + " to " + dest);
        if (!isDryRun) {
            try {
                Files.copy(path, dest);
            } catch (IOException e) {
                logger.error("Error occurred while moving file " + path + " to " + dest, e);
            }
        }
    }

    private void move(Path path, boolean isDryRun, Path dest, OverwriteOperation overwriteOperation) {
        logger.info(overwriteOperation.logPrefix + "Moving " + path + " to " + dest);
        if (!isDryRun) {
            try {
                Files.move(path, dest);
            } catch (IOException e) {
                logger.error("Error occurred while moving file " + path + " to " + dest, e);
            }
        }
    }


    protected static class OverwriteOperation {
        private boolean shouldProceed;
        private Path destination;
        private String logPrefix;

        protected OverwriteOperation(boolean shouldProceed, Path destination, String logPrefix) {
            this.shouldProceed = shouldProceed;
            this.destination = destination;
            this.logPrefix = logPrefix;
        }
    }
}

