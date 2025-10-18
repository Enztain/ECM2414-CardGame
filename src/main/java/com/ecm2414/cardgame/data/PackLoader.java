package com.ecm2414.cardgame.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Loads the pack file used to start the card game.
 * Delegates all validation to PackValidator.
 */
public final class PackLoader {

    private PackLoader() {} // prevent creating instances

    /**
     * Reads the pack file and checks it is valid.
     *
     * @param path the path to the pack file
     * @param n    the number of players
     * @return an unmodifiable list of card values from the file
     * @throws InvalidPackException if the pack is not valid
     * @throws IOException          if the file cannot be read
     */
    public static List<Integer> load(Path path, int n) throws IOException, InvalidPackException {
        PackValidator.validatePlayerCount(n);

        List<Integer> fileContent = new ArrayList<>(8 * n);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                fileContent.add(PackValidator.parseNonNegativeInt(line, lineNo));
            }
        }

        PackValidator.validateLineCount(fileContent, n);
        return Collections.unmodifiableList(fileContent);
    }
}
