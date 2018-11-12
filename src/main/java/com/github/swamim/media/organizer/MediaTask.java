package com.github.swamim.media.organizer;

import com.github.swamim.media.exiftool.core.ExifDateData;
import com.github.swamim.media.exiftool.core.ExifParser;
import com.github.swamim.media.organizer.files.OverwriteMode;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Set;


public class MediaTask implements Runnable {

    private static Logger logger = Logger.getLogger(MediaTask.class);

    private final Path source;
    private final OrganizerConfiguration configuration;
    private final ExifParser exifParser;
    private final TaskMonitor taskMonitor;
    private final Set<String> destinationDirectories;

    public MediaTask(Path source, OrganizerConfiguration configuration, ExifParser parser,
                     Set<String> destinationDirectories,
                     TaskMonitor taskMonitor) {
        this.source = source;
        this.configuration = configuration;
        this.exifParser = parser;
        this.destinationDirectories = destinationDirectories;
        this.taskMonitor = taskMonitor;
    }

    @Override
    public void run() {
        processMedia();
    }

    private void processMedia() {
        try {
            ExifDateData exifDateData = exifParser.parse(source.toFile());

            String folderName = exifDateData.getFolderName();
            if (folderName == null) {
                folderName = OrganizerConfiguration.FOLDER_NAME_FOR_MEDIA_WITH_NO_EXIF_DATA;
            }
            copyFiles(folderName);
        } catch (Exception e) {
            logger.error("Error while processing Media. Copying it to Default Non Exif Folder.", e);
            copyFiles(OrganizerConfiguration.FOLDER_NAME_FOR_MEDIA_WITH_NO_EXIF_DATA);
        } finally {
            taskMonitor.pop();
        }
    }

    private void copyFiles(String subFolderName) {
        OverwriteMode overwriteMode = configuration.getOverwriteMode();
        overwriteMode.getCopier().copyFiles(source, destinationDirectories, subFolderName, configuration.getCopyMode());
    }

}
