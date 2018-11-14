package com.github.swamim.media.exiftool.core;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Arrays.asList;

public class ExifParser {
    private static final Logger logger = Logger.getLogger(ExifParser.class);
    private final int numInstances;

    private ExifTool exifTool;

    public ExifParser(String exifToolPath, int numInstances) {
        System.setProperty("exiftool.path", exifToolPath);
        this.numInstances = numInstances;
    }

    public void init() {
        try {
            exifTool = new ExifToolBuilder()
                    .withPoolSize(numInstances)
                    .enableStayOpen()
                    .build();
        } catch (UnsupportedFeatureException ex) {
            logger.error("Some of the Exif tool features not supported, fall back to simple exif tool", ex);
            exifTool = new ExifToolBuilder().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ExifDateData parse(File media) throws IOException {
        Map<Tag, String> map = exifTool.getImageMeta(media,
                asList(
                        StandardTag.DATE_TIME_ORIGINAL,
                        CustomTag.MODIFIED_DATE,
                        StandardTag.CREATION_DATE
                )
        );
        ExifDateData exifDateData = new ExifDateData(map.get(StandardTag.DATE_TIME_ORIGINAL), map.get(CustomTag.MODIFIED_DATE),
                map.get(StandardTag.CREATION_DATE));
        return exifDateData;
    }

    public void shutdown() {
        if (exifTool != null) {
            try {
                exifTool.close();
                logger.info("Exif Tool shut down");
            } catch (Exception e) {
                logger.error("Exception while shutting down Exif Tool!!!!", e);
            }
        }
    }
}