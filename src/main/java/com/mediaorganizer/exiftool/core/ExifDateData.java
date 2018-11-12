package com.mediaorganizer.exiftool.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExifDateData {
    private static final String ZERO_DATE = "0000:00:00 00:00:00";

    private final String originalDate;
    private final String modifiedDate;
    private final String creationDate;


    public ExifDateData(String originalDate, String modifiedDate, String creationDate) {
        if (originalDate != null && !ZERO_DATE.equals(originalDate.trim())) {
            this.originalDate = originalDate.substring(0, 10).replace(':', '-');
        } else {
            this.originalDate = originalDate;
        }
        if (modifiedDate != null && !ZERO_DATE.equals(modifiedDate.trim())) {
            this.modifiedDate = modifiedDate.substring(0, 10).replace(':', '-');
        } else {
            this.modifiedDate = modifiedDate;
        }
        if (creationDate != null && !ZERO_DATE.equals(creationDate.trim())) {
            this.creationDate = creationDate.substring(0, 10).replace(':', '-');
        } else {
            this.creationDate = creationDate;
        }
    }

    public String getFolderName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (originalDate != null) {
            return originalDate;
        }
        if (modifiedDate == null && creationDate == null) {
            return null;
        }
        if (modifiedDate == null) {
            return creationDate;
        }
        if (creationDate == null) {
            return modifiedDate;
        }
        try {
            Date modDate = dateFormat.parse(modifiedDate);
            Date creDate = dateFormat.parse(creationDate);
            if (modDate.before(creDate)) {
                return modifiedDate;
            } else {
                return creationDate;
            }
        } catch (ParseException e) {
            return null;
        }
    }
}
