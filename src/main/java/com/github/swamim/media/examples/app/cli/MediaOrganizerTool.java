package com.github.swamim.media.examples.app.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.github.swamim.media.organizer.MediaOrganizerBuilder;
import com.github.swamim.media.organizer.files.CopyMode;
import com.google.common.collect.Sets;
import com.github.swamim.media.organizer.MediaOrganizer;
import com.github.swamim.media.organizer.files.OverwriteMode;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class MediaOrganizerTool {

    private static Logger logger;

    static {
        System.setProperty("current.date.time", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
        logger = Logger.getLogger(MediaOrganizerTool.class);
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            logger.error("Error " + t, e);
        });
    }

    @Parameter(names = {"--exiftoolpath", "-exif"}, description = "Path to the EXIF tool", required = true, order = 0)
    private String exifToolPath;

    @Parameter(names = {"--sourcedirectory", "-from"}, description = "Source directories", required = true, order = 1)
    private Set<String> sourceDirectories;

    @Parameter(names = {"--targetimagesdirectory", "-imagesto"}, description = "Target Image directories", required = true, order = 2)
    private Set<String> targetImageDirectories;

    @Parameter(names = {"--targetvideosdirectory", "-videosto"}, description = "Target Videos directories", required = true, order = 3)
    private Set<String> targetVideoDirectories;

    @Parameter(names = {"--copymode", "-c"}, description = "Copy Options", order = 4)
    private CopyMode copyMode = CopyMode.COPY_DRY_RUN;

    @Parameter(names = {"--overwrite", "-o"}, description = "Overwrite Options", order = 5)
    private OverwriteMode overwriteMode = OverwriteMode.DO_NOT_OVERWRITE;

    @Parameter(names = {"--imagetype", "-it"}, description = "Image File Extensions", order = 6)
    private Set<String> imageFileExtensions = Sets.newHashSet("png", "gif", "jpg", "jpeg", "tiff");

    @Parameter(names = {"--videotype", "-vt"}, description = "Video File Extensions", order = 7)
    private Set<String> videoFileExtensions = Sets.newHashSet("vob", "webm", "mkv", "wmv", "mpeg", "mpg", "flv", "mp4", "mts", "mov", "3gp", "avi");

    @Parameter(names = {"-h", "--help"}, help = true, order = 8)
    private boolean help;


    public static void main(String... argv) {

        MediaOrganizerTool main = new MediaOrganizerTool();
        JCommander commander = JCommander.newBuilder()
                .addObject(main)
                .build();

        commander.setProgramName(MediaOrganizerTool.class.getSimpleName());
        try {
            commander.parse(argv);
        } catch (ParameterException e) {
            commander.usage();
            return;
        }
        if (main.help) {
            commander.usage();
            return;
        }
        main.run(commander);
    }

    public void run(JCommander commander) {
        MediaOrganizer organizer;
        MediaOrganizerBuilder builder = new MediaOrganizerBuilder();
        try {
            builder.
                    usingExifTool(exifToolPath).
                    forImageFileExtensions(imageFileExtensions.toArray(new String[imageFileExtensions.size()])).
                    forVideoFileExtensions(videoFileExtensions.toArray(new String[videoFileExtensions.size()])).
                    fromSource(sourceDirectories.toArray(new String[sourceDirectories.size()])).
                    saveImagesTo(targetImageDirectories.toArray(new String[targetImageDirectories.size()])).
                    saveVideosTo(targetVideoDirectories.toArray(new String[targetVideoDirectories.size()])).
                    usingOverwriteMode(overwriteMode).
                    usingCopyMode(copyMode);
            organizer = builder.build();
        } catch (RuntimeException re) {
            logger.error(re.getMessage());
            commander.usage();
            return;
        }
        organizer.run();
    }
}
