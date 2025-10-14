package com.ecm2414.cardgame.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Loads and checks the pack file used to start the card game.
 * <p>
 * The file must have 8 × n lines (where n = number of players),
 * and each line must contain a single non-negative integer.
 */
public final class PackLoader {

    private PackLoader() {} // prevent creating instances

    /**
     * Reads the pack file and checks it is valid.
     *
     * @param path the path to the pack file
     * @param n    the number of players
     * @return a list of card values from the file
     * @throws InvalidPackException if the pack is not valid
     * @throws IOException if the file cannot be read
     */
    public static List<Integer> load(Path path, int n) throws IOException, InvalidPackException {
        validatePlayerCount(n);

        List<Integer> fileContent = new ArrayList<>(8 * n);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                fileContent.add(parseNonNegativeInt(line, lineNo));
            }
        }

        validateLineCount(fileContent, n);
        return Collections.unmodifiableList(fileContent);
    }

    /**
     * Checks that the number of players is greater than zero.
     */
    private static void validatePlayerCount(int n) throws InvalidPackException {
        if (n <= 0) {
            throw new InvalidPackException("Number of players must be greater than 0.");
        }
    }

    /**
     * Checks that the file has the right number of lines (8 × n).
     */
    private static void validateLineCount(List<Integer> lines, int n) throws InvalidPackException {
        int expected = 8 * n;
        int actual = lines.size();
        if (actual == 0) {
            throw new InvalidPackException("Pack file is empty; expected " + expected + " lines.");
        }
        if (actual != expected) {
            throw new InvalidPackException("Pack file has " + actual + " lines; expected " + expected + ".");
        }
    }

    /**
     * Checks and converts one line to a non-negative integer.
     */
    private static int parseNonNegativeInt(String raw, int lineNo) throws InvalidPackException {
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

    /**
     * Exception thrown when a pack file is invalid.
     */
    public static class InvalidPackException extends Exception {
        public InvalidPackException(String message) {
            super(message);
        }
    }
}
