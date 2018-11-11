package com.mediaorganizer.organizer.files;

public enum CopyMode {
    COPY(1),
    MOVE(2),
    COPY_DRY_RUN(3),
    MOVE_DRY_RUN(4);

    private final int id;

    CopyMode(int i) {
        this.id = i;
    }

    public static CopyMode getById(int id) {
        for (CopyMode mode : values()) {
            if (mode.id == id) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid Id " + id);
    }
}
