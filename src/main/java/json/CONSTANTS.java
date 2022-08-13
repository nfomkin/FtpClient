package json;

public enum CONSTANTS {
    CURLY_OPEN_BRACKETS('{'),
    CURLY_CLOSE_BRACKETS('}'),
    SQUARE_OPEN_BRACKETS('['),
    SQUARE_CLOSE_BRACKETS(']'),
    COLON(':'),
    COMMA(','),
    SPECIAL('|');

    private final char constant;

    // Constructor
    CONSTANTS(char constant) { this.constant = constant; }

    // Method
    // Overriding exiting toString() method
    @Override public String toString()
    {
        return String.valueOf(constant);
    }
}
