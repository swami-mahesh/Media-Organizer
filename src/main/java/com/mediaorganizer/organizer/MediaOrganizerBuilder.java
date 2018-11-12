package com.mediaorganizer.organizer;

import com.google.common.collect.Sets;
import com.mediaorganizer.organizer.files.CopyMode;
import com.mediaorganizer.organizer.files.OverwriteMode;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MediaOrganizerBuilder {

    private static final Logger logger = Logger.getLogger(MediaTask.class);

    private String exifToolPath;
    private final Set<String> imageFileExtensions = Sets.newHashSet("png", "gif", "jpg", "jpeg", "tiff");
    private final Set<String> videoFileExtensions = Sets.newHashSet("vob", "webm","mkv", "wmv", "mpeg", "mpg", "flv", "mp4", "mts", "mov", "3gp", "avi");
    private final Set<String> sourceDirectories = new HashSet<>();
    private final Set<String> targetImageDirectories = new HashSet<>();
    private final Set<String> targetVideoDirectories = new HashSet<>();
    private CopyMode copyMode = CopyMode.COPY_DRY_RUN;
    private OverwriteMode overwriteMode = OverwriteMode.DO_NOT_OVERWRITE;

    public MediaOrganizerBuilder usingExifTool(String exifToolPath) {
        this.exifToolPath = exifToolPath;
        return this;
    }

    public MediaOrganizerBuilder forImageFileExtensions(String... extensions) {
        if (extensions != null) {
            Arrays.stream(extensions).forEach(this.imageFileExtensions::add);
        }
        return this;
    }

    public MediaOrganizerBuilder forVideoFileExtensions(String... extensions) {
        if (extensions != null) {
            Arrays.stream(extensions).forEach(this.videoFileExtensions::add);
        }
        return this;
    }

    public MediaOrganizerBuilder fromSource(String... directories) {
        if (directories != null) {
            Arrays.stream(directories).forEach(this.sourceDirectories::add);
        }
        return this;
    }

    public MediaOrganizerBuilder saveImagesTo(String... directories) {
        if (directories != null) {
            Arrays.stream(directories).forEach(this.targetImageDirectories::add);
        }
        return this;
    }

    public MediaOrganizerBuilder saveVideosTo(String... directories) {
        if (directories != null) {
            Arrays.stream(directories).forEach(this.targetVideoDirectories::add);
        }
        return this;
    }

    public MediaOrganizerBuilder usingCopyMode(CopyMode copyMode) {
        Objects.requireNonNull(copyMode);
        this.copyMode = copyMode;
        return this;
    }

    public MediaOrganizerBuilder usingOverwriteMode(OverwriteMode overwriteMode) {
        Objects.requireNonNull(overwriteMode);
        this.overwriteMode = overwriteMode;
        return this;
    }

    public MediaOrganizer build() {
        validateSourceDirectories();
        validateTargetDirectories();
        validateExifTool();
        OrganizerConfiguration configuration = new OrganizerConfiguration(exifToolPath, imageFileExtensions, videoFileExtensions,
                sourceDirectories, targetImageDirectories, targetVideoDirectories, copyMode, overwriteMode);
        logger.info(configuration);

        return new MediaOrganizer(configuration);
    }

    private void validateSourceDirectories() {
        if (sourceDirectories.isEmpty()) {
            throw new IllegalArgumentException("At least one valid source directory should be specified");
        }
        if (sourceDirectories.stream().map(Paths::get).filter(path -> !Files.exists(path)).count() > 0) {
            throw new IllegalArgumentException("At least one of the source directories does not exist." + sourceDirectories);
        }
        if (sourceDirectories.stream().map(Paths::get).filter(path -> !path.toFile().isDirectory()).count() > 0) {
            throw new IllegalArgumentException("Only directories allowed in fromSource() arguments." + sourceDirectories);
        }
    }

    private void validateTargetDirectories() {
        if (targetImageDirectories.isEmpty()) {
            throw new IllegalArgumentException("At least one valid target image directory should be specified");
        }
        if (targetImageDirectories.stream().map(Paths::get).filter(path -> !path.toFile().isDirectory()).count() > 0) {
            throw new IllegalArgumentException("Only directories allowed in saveImagesTo() arguments" + targetImageDirectories);
        }
        if (targetImageDirectories.stream().map(Paths::get).filter(path -> !Files.exists(path)).count() > 0) {
            throw new IllegalArgumentException("At least one of the target image directories does not exist." + targetImageDirectories);
        }
        if (targetVideoDirectories.isEmpty()) {
            throw new IllegalArgumentException("At least one valid target video directory should be specified");
        }
        if (targetVideoDirectories.stream().map(Paths::get).filter(path -> !Files.exists(path)).count() > 0) {
            throw new IllegalArgumentException("At least one of the target video directories does not exist." + targetVideoDirectories);
        }
        if (targetVideoDirectories.stream().map(Paths::get).filter(path -> !path.toFile().isDirectory()).count() > 0) {
            throw new IllegalArgumentException("Only directories allowed  in saveVideosTo() arguments" + targetVideoDirectories);
        }
    }

    private void validateExifTool() {
        if (exifToolPath == null || exifToolPath.trim().equals("")) {
            throw new IllegalArgumentException("Path to the Exif tool must be specified.");
        }
        try {
            System.setProperty("exiftool.path", exifToolPath);
            ExifTool exifTool = new ExifToolBuilder().build();
            exifTool.close();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Exif tool does not exists at the path provided: " + exifToolPath);
        }
    }

}
