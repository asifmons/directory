package com.stjude.directory.enums;

import lombok.Getter;

@Getter
public enum BloodGroup {
    A_POSITIVE("A +ve"),
    A_NEGATIVE("A -ve"),
    B_POSITIVE("B +ve"),
    B_NEGATIVE("B -ve"),
    AB_POSITIVE("AB +ve"),
    AB_NEGATIVE("AB -ve"),
    O_POSITIVE("O +ve"),
    O_NEGATIVE("O -ve");

    // Getter for the display value
    private final String displayValue;

    // Constructor to set the display value
    BloodGroup(String displayValue) {
        this.displayValue = displayValue;
    }

    // Optional: Override toString to return the display value
    @Override
    public String toString() {
        return displayValue;
    }

    public static BloodGroup getNameForDisplayValue(String displayValue) {
        for (BloodGroup bloodGroup : BloodGroup.values()) {
            if (bloodGroup.displayValue.equalsIgnoreCase(displayValue)) {
                return bloodGroup; // Return the enum name
            }
        }
        return null;
    }
}

