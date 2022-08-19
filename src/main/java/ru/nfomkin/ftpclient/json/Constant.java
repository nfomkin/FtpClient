package ru.nfomkin.ftpclient.json;

public enum Constant {
    CURLY_OPEN_BRACKETS('{'),
    CURLY_CLOSE_BRACKETS('}'),
    SQUARE_OPEN_BRACKETS('['),
    SQUARE_CLOSE_BRACKETS(']'),
    COLON(':'),
    COMMA(','),
    ARRAY_FIELDS_DELIMITER('|'),
    ARRAY_OBJECTS_DELIMITER(';');

    private final char constant;

    // Constructor
    Constant(char constant) { this.constant = constant; }

    // Method
    // Overriding exiting toString() method
    @Override public String toString()
    {
        return String.valueOf(constant);
    }
}
