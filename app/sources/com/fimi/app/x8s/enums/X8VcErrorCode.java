package com.fimi.app.x8s.enums;

public enum X8VcErrorCode {
    TRACKER_NO_ERROR(1),
    TRACKER_INVALID_ARGUMET(2),
    TARGET_TOO_BIG(4),
    TARGET_TOO_SMALL(8),
    TARGET_NOT_OBVIOUS(16),
    IMAGE_TOO_DARK(32),
    IMAGE_TOO_LIGHT(64),
    LOW_CONFIDENCE(128),
    TARGET_MISSED(256),
    OBJ_RECOVERY(512),
    TRACK_FAILED(1024),
    TARGET_IS_PERSON(4096),
    TRACKER_BUSY(Integer.MIN_VALUE);
    
    private int value;

    private X8VcErrorCode(int value) {
        this.value = value;
    }

    public static X8VcErrorCode valueOf(int value) {
        switch (value) {
            case Integer.MIN_VALUE:
                return TRACKER_BUSY;
            case 1:
                return TRACKER_NO_ERROR;
            case 2:
                return TRACKER_INVALID_ARGUMET;
            case 4:
                return TARGET_TOO_BIG;
            case 8:
                return TARGET_TOO_SMALL;
            case 16:
                return TARGET_NOT_OBVIOUS;
            case 32:
                return IMAGE_TOO_DARK;
            case 64:
                return IMAGE_TOO_LIGHT;
            case 128:
                return LOW_CONFIDENCE;
            case 256:
                return TARGET_MISSED;
            case 512:
                return OBJ_RECOVERY;
            case 1024:
                return TRACK_FAILED;
            case 4096:
                return TARGET_IS_PERSON;
            default:
                return null;
        }
    }
}
