package com.ecm2414.cardgame.data;

import java.util.List;

/**
 * Validates the pack and its related inputs.
 * Keeps all validation concerns out of PackLoader.
 */
public final class PackValidator {

    private PackValidator() {} // utility class

    /** Checks that the number of players is greater than zero. */
    public static void validatePlayerCount(int n) throws InvalidPackException {
        if (n <= 0) {
            throw new InvalidPackException("Number of players must be greater than 0.");
        }
    }

    /** Checks that the file has the right number of lines (8 × n). */
    public static void validateLineCount(List<Integer> lines, int n) throws InvalidPackException {
        int expected = 8 * n;
        int actual = lines.size();
        if (actual == 0) {
            throw new InvalidPackException("Pack file is empty; expected " + expected + " lines.");
        }
        if (actual != expected) {
            throw new InvalidPackException("Pack file has " + actual + " lines; expected " + expected + ".");
        }
    }

    /** Checks and converts one line to a non-negative integer. */
    public static int parseNonNegativeInt(String raw, int lineNo) throws InvalidPackException {
        String s = raw.trim();
        if (s.isEmpty()) {
            throw new InvalidPackException("Line " + lineNo + " is blank.");
        }
        try {
            int v = Integer.parseInt(s);
            if (v < 0) {
                throw new InvalidPackException("Line " + lineNo + " is negative (" + v + ").");
            }
            return v;
        } catch (NumberFormatException e) {
            throw new InvalidPackException("Line " + lineNo + " is not a valid integer: \"" + s + "\"");
        }
    }
}
