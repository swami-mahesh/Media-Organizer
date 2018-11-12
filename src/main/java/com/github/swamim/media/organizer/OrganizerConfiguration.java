package com.github.swamim.media.organizer;

import com.github.swamim.media.organizer.files.CopyMode;
import com.github.swamim.media.organizer.files.OverwriteMode;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class OrganizerConfiguration {

    static final String FOLDER_NAME_FOR_MEDIA_WITH_NO_EXIF_DATA = "NOEXIF";

    private final String exifToolPath;
    private final Set<String> imageFileExtensions;
    private final Set<String> videoFileExtensions;
    private final Set<String> sourceDirectories;
    private final Set<String> targetImageDirectories;
    private final Set<String> targetVideoDirectories;
    private final CopyMode copyMode;
    private final OverwriteMode overwriteMode;

    OrganizerConfiguration(String exifToolPath, Set<String> imageFileExtensions, Set<String> videoFileExtensions,
                           Set<String> sourceDirectories, Set<String> targetImageDirectories, Set<String> targetVideoDirectories,
                           CopyMode copyMode, OverwriteMode overwriteMode) {
        this.exifToolPath = exifToolPath;
        this.imageFileExtensions = Collections.unmodifiableSet(new HashSet<>(imageFileExtensions));
        this.videoFileExtensions = Collections.unmodifiableSet(new HashSet<>(videoFileExtensions));
        this.sourceDirectories = Collections.unmodifiableSet(new HashSet<>(sourceDirectories));
        this.targetImageDirectories = Collections.unmodifiableSet(new HashSet<>(targetImageDirectories));
        this.targetVideoDirectories = Collections.unmodifiableSet(new HashSet<>(targetVideoDirectories));
        this.copyMode = copyMode;
        this.overwriteMode = overwriteMode;
    }

    String getExifToolPath() {
        return exifToolPath;
    }

    Set<String> getImageFileExtensions() {
        return imageFileExtensions;
    }

    Set<String> getVideoFileExtensions() {
        return videoFileExtensions;
    }

    Set<String> getSourceDirectories() {
        return sourceDirectories;
    }

    Set<String> getTargetImageDirectories() {
        return targetImageDirectories;
    }

    Set<String> getTargetVideoDirectories() {
        return targetVideoDirectories;
    }

    CopyMode getCopyMode() {
        return copyMode;
    }

    OverwriteMode getOverwriteMode() {
        return overwriteMode;
    }

    @Override
    public String toString() {
        return "\r\n***************************************************************   CONFIGURATION   ***************************************************************\r\n" +
                "* EXIF Tool Path                           : " + exifToolPath + "\r\n" +
                "* Image File Extensions                    : " + imageFileExtensions + "\r\n" +
                "* Video File Extensions                    : " + videoFileExtensions + "\r\n" +
                "* Media Source Directories                 : " + sourceDirectories + "\r\n" +
                "* Save Images to Directories               : " + targetImageDirectories + "\r\n" +
                "* Save Videos to Directories               : " + targetVideoDirectories + "\r\n" +
                "* Save Non Exif Images to Directories      : " + targetImageDirectories.stream().map(d -> d + File.separator + FOLDER_NAME_FOR_MEDIA_WITH_NO_EXIF_DATA).collect(Collectors.toSet()) + "\r\n" +
                "* Save Non Exif Videos to Directories      : " + targetVideoDirectories.stream().map(d -> d + File.separator + FOLDER_NAME_FOR_MEDIA_WITH_NO_EXIF_DATA).collect(Collectors.toSet()) + "\r\n" +
                "* COPY/ MOVE Media from source to target   : " + copyMode + "\r\n" +
                "* Overwrite Mode                           : " + overwriteMode + "\r\n" +
                "*************************************************************************************************************************************************\r\n";
    }
}

