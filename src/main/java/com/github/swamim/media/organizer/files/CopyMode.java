package com.github.swamim.media.organizer.files;

public enum CopyMode {
    COPY(1, false),
    MOVE(2, false),
    COPY_DRY_RUN(3, true),
    MOVE_DRY_RUN(4, true);

    private final int id;
    private final boolean isDryRun;

    CopyMode(int i, boolean isDryRun) {
        this.id = i;
        this.isDryRun = isDryRun;
    }

    public boolean isDryRun() {
        return isDryRun;
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
