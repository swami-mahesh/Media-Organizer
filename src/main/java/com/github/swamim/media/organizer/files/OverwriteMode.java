package com.github.swamim.media.organizer.files;

import com.github.swamim.media.organizer.files.copier.*;

public enum OverwriteMode {

    DO_NOT_OVERWRITE(1, new NoOverwriteCopier()),
    DO_NOT_OVERWRITE_COPY(2, new NoOverwriteButCopyCopier()),
    OVERWRITE_ONLY_IF_FILE_SIZE_MATCH_ELSE_COPY(3, new FileSizeMatchOverwriteCopier()),
    OVERWRITE_ONLY_IF_EXACT_SAME_FILE_ELSE_COPY(4, new FileContentMatchOverwriteCopier()),
    ALWAYS_OVERWRITE(5, new AlwaysOverwriteCopier());

    private final AbstractCopier copier;
    private final int id;

    OverwriteMode(int id, AbstractCopier copier) {
        this.id = id;
        this.copier = copier;
    }

    public AbstractCopier getCopier() {
        return copier;
    }

    public static OverwriteMode getById(int id) {
        for (OverwriteMode mode : values()) {
            if (mode.id == id) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid Id " + id);
    }
}
