package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum MediaType {
    // Image types
    IMAGE("image"),
//    IMAGE_PNG("image/png"),
//    IMAGE_GIF("image/gif"),
//    IMAGE_BMP("image/bmp"),
//    IMAGE_WEBP("image/webp"),

    // Video types
    VIDEO("video");
//    VIDEO_MPEG("video/mpeg"),
//    VIDEO_QUICKTIME("video/quicktime"),
//    VIDEO_AVI("video/x-msvideo"),
//    VIDEO_MKV("video/x-matroska"),
//    VIDEO_WEBM("video/webm");

    private final String mimeType;

    MediaType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static boolean isSupported(String mimeType) {
        for (MediaType type : values()) {
            if (type.getMimeType().equalsIgnoreCase(mimeType)) {
                return true;
            }
        }
        return false;
    }
}
