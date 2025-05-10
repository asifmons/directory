package com.stjude.directory.enums;

public enum Unit {
    ST_MARY("St. Mary"),
    ST_SEBASTIAN("St. Sebastian"),
    ST_THOMAS("St. Thomas"),
    INFANT_JESUS("Infant Jesus"),
    ST_JOSEPH("St. Joseph"),
    ST_ANTONY("St.Antony"),
    HOLY_FAMILY("Holy Family"),
    LITTLE_FLOWER("Little Flower"),
    ST_GEORGE("St. George");

    private final String displayValue;

    // Constructor to assign the display value
    Unit(String displayValue) {
        this.displayValue = displayValue;
    }

    // Getter for the display value
    public String getDisplayValue() {
        return displayValue;
    }

    // Static method to get Unit by display value
    public static Unit getByDisplayValue(String displayValue) {
        for (Unit unit : Unit.values()) {
            if (unit.displayValue.equalsIgnoreCase(displayValue)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("No Unit with display value: " + displayValue);
    }

    @Override
    public String toString() {
        return displayValue;
    }
}
