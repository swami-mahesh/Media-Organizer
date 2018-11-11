package com.mediaorganizer.organizer.config;

import com.mediaorganizer.organizer.files.CopyMode;
import com.mediaorganizer.organizer.files.OverwriteMode;

import java.util.Set;

public class YamlConfiguration {
    private Set<String> imageFileExtensions;
    private Set<String> videoFileExtensions;
    private Set<String> sourceDirectories;
    private Set<String> imageBackupDirectories;
    private Set<String> videoBackupDirectories;
    private String exifToolPath;
    private CopyMode copyMode;
    private OverwriteMode overwriteMode;


    public Set<String> getImageFileExtensions() {
        return imageFileExtensions;
    }

    public void setImageFileExtensions(Set<String> imageFileExtensions) {
        this.imageFileExtensions = imageFileExtensions;
    }

    public Set<String> getVideoFileExtensions() {
        return videoFileExtensions;
    }

    public void setVideoFileExtensions(Set<String> videoFileExtensions) {
        this.videoFileExtensions = videoFileExtensions;
    }

    public Set<String> getSourceDirectories() {
        return sourceDirectories;
    }

    public void setSourceDirectories(Set<String> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public Set<String> getImageBackupDirectories() {
        return imageBackupDirectories;
    }

    public void setImageBackupDirectories(Set<String> imageBackupDirectories) {
        this.imageBackupDirectories = imageBackupDirectories;
    }

    public Set<String> getVideoBackupDirectories() {
        return videoBackupDirectories;
    }

    public void setVideoBackupDirectories(Set<String> videoBackupDirectories) {
        this.videoBackupDirectories = videoBackupDirectories;
    }

    public String getExifToolPath() {
        return exifToolPath;
    }

    public void setExifToolPath(String exifToolPath) {
        this.exifToolPath = exifToolPath;
    }

    public CopyMode getCopyMode() {
        return copyMode;
    }

    public void setCopyMode(CopyMode copyMode) {
        this.copyMode = copyMode;
    }

    public OverwriteMode getOverwriteMode() {
        return overwriteMode;
    }

    public void setOverwriteMode(OverwriteMode overwriteMode) {
        this.overwriteMode = overwriteMode;
    }
}
