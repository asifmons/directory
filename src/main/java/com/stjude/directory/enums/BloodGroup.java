package com.stjude.directory.enums;

public enum BloodGroup {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String displayValue;

    // Constructor to set the display value
    BloodGroup(String displayValue) {
        this.displayValue = displayValue;
    }

    // Getter for the display value
    public String getDisplayValue() {
        return displayValue;
    }

    // Optional: Override toString to return the display value
    @Override
    public String toString() {
        return displayValue;
    }
}

