package com.stjude.directory.enums;

public enum FeedbackReason {
    GENERAL("General"),
    BUG_REPORT("Bug Report"),
    FEATURE_REQUEST("Feature Request"),
    USER_INTERFACE("User Interface"),
    PERFORMANCE("Performance"),
    CONTENT_ISSUE("Content Issue"),
    OTHER("Other"),
    DATA_ISSUE("Data Issue");

    private final String displayName;

    FeedbackReason(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static FeedbackReason fromDisplayName(String displayName) {
        for (FeedbackReason reason : FeedbackReason.values()) {
            if (reason.getDisplayName().equalsIgnoreCase(displayName)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Unknown feedback reason: " + displayName);
    }
}
