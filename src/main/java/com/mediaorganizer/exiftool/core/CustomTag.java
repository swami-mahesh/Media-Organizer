package com.mediaorganizer.exiftool.core;

import com.thebuzzmedia.exiftool.Tag;


public enum CustomTag implements Tag {

    MODIFIED_DATE("ModifyDate", MyType.STRING);

    private final String name;
    private final MyType type;

    CustomTag(String name, MyType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public <T> T parse(String value) {
        return this.type.parse(value);
    }

    static enum MyType {
        INTEGER {
            public <T> T parse(String value) {
                return (T) Integer.valueOf(Integer.parseInt(value));
            }
        },
        DOUBLE {
            public <T> T parse(String value) {
                return (T) Double.valueOf(Double.parseDouble(value));
            }
        },
        STRING {
            public <T> T parse(String value) {
                return (T) value;
            }
        };

        private MyType() {
        }

        public abstract <T> T parse(String var1);
    }
}
