package com.github.swamim.media.exiftool.core;

import com.thebuzzmedia.exiftool.Tag;

/**
 * This class supports additional EXIF tags not supported by
 * com.thebuzzmedia.exiftool.Tag
 * Supported EXIF tags :
 * 1. ModifyDate
 */

public enum CustomTag implements Tag {

    MODIFIED_DATE("ModifyDate", TagType.STRING);

    private final String name;
    private final TagType type;

    CustomTag(String name, TagType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public <T> T parse(String value) {
        return this.type.parse(value);
    }

    enum TagType {

        STRING {
            public <T> T parse(String value) {
                return (T) value;
            }
        };

        public abstract <T> T parse(String var1);
        }
}
