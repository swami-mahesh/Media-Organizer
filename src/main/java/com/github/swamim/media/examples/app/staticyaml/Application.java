package com.github.swamim.media.examples.app.staticyaml;

import com.github.swamim.media.organizer.MediaOrganizer;
import com.github.swamim.media.organizer.MediaOrganizerBuilder;
import com.github.swamim.media.organizer.config.YamlConfiguration;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Application {
    private static Logger logger;
    private MediaOrganizer mediaOrganizer;

    static {
        System.setProperty("current.date.time", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
        logger = Logger.getLogger(Application.class);
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            logger.error("Error " + t, e);
        });
    }

    private void run(String configFileName) {
        YamlConfiguration yamlConfiguration = getConfiguration(configFileName);

        MediaOrganizerBuilder builder = new MediaOrganizerBuilder();
        builder.
                usingExifTool(yamlConfiguration.getExifToolPath()).
                forImageFileExtensions(yamlConfiguration.getImageFileExtensions().toArray(new String[yamlConfiguration.getImageFileExtensions().size()])).
                forVideoFileExtensions(yamlConfiguration.getVideoFileExtensions().toArray(new String[yamlConfiguration.getVideoFileExtensions().size()])).
                fromSource(yamlConfiguration.getSourceDirectories().toArray(new String[yamlConfiguration.getSourceDirectories().size()])).
                saveImagesTo(yamlConfiguration.getImageBackupDirectories().toArray(new String[yamlConfiguration.getImageBackupDirectories().size()])).
                saveVideosTo(yamlConfiguration.getVideoBackupDirectories().toArray(new String[yamlConfiguration.getVideoBackupDirectories().size()])).
                usingOverwriteMode(yamlConfiguration.getOverwriteMode()).
                usingCopyMode(yamlConfiguration.getCopyMode());
        mediaOrganizer = builder.build();
        mediaOrganizer.run();
    }

    private YamlConfiguration getConfiguration(String configFileName) {
        Yaml yaml = new Yaml(new Constructor(YamlConfiguration.class));
        InputStream inputStream = Application.class
                .getClassLoader()
                .getResourceAsStream(configFileName);
        return yaml.load(inputStream);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage : java com.mediaorganizer.organizer.app.staticyaml.Application <YAML CONFIG FILE>");
            System.exit(1);
        }
        Application application = new Application();
        application.run(args[0]);
    }

}
