package com.ecm2414.cardgame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckLoggerTest {

    private final Path cwd = Path.of("").toAbsolutePath();

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(cwd.resolve("deck2_output.txt"));
        Files.deleteIfExists(cwd.resolve("deck5_output.txt"));
    }

    @Test
    void logFinalState_writesSingleLineWithContents_exactFormat() throws Exception {
        DeckLogger.logFinalState(2, List.of(1, 3, 3, 7));

        Path file = cwd.resolve("deck2_output.txt");
        assertTrue(Files.exists(file), "deck2_output.txt should be created in the working directory");

        String line = Files.readString(file).trim();
        assertEquals("deck2 contents: 1 3 3 7", line);
    }

    @Test
    void logFinalState_handlesEmptyDeck() throws Exception {
        DeckLogger.logFinalState(5, List.of());

        Path file = cwd.resolve("deck5_output.txt");
        assertTrue(Files.exists(file), "deck5_output.txt should be created in the working directory");

        String line = Files.readString(file).trim();
        assertEquals("deck5 contents:", line);
    }
}