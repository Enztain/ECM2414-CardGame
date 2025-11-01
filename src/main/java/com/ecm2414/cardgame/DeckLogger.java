package com.ecm2414.cardgame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Writes the final contents of a deck to an output file at the end of the game.
 *
 * Example output file (deck1_output.txt):
 * deck1 contents: 3 3 4 7
 */
public final class DeckLogger {

    private DeckLogger() {} // utility class, no instances

    /**
     * Writes the final deck state to a file named "deck<ID>_output.txt".
     *
     * @param deckId   the ID of the deck (e.g., 1 for deck1)
     * @param contents the final list of card values in the deck
     * @throws IOException if the file cannot be written
     */
    public static void logFinalState(int deckId, List<Integer> contents) throws IOException {
        String fileName = "deck" + deckId + "_output.txt";
        Path path = Path.of(fileName);

        StringBuilder sb = new StringBuilder();
        sb.append("deck").append(deckId).append(" contents:");
        for (Integer card : contents) {
            sb.append(" ").append(card);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(sb.toString());
            writer.newLine();
        }
    }

}
