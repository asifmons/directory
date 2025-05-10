package com.stjude.directory.enums;

public enum Status {
    ACTIVE("Active"),
    EXPIRED("Expired");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static Status getByDisplayValue(String value) {
        for (Status status : Status.values()) {
            if (status.displayValue.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}