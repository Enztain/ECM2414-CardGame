package com.ecm2414.cardgame;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerLoggerTest {

    Path file;

    @AfterEach
    void cleanup() throws Exception {
        if (file != null) Files.deleteIfExists(file);
    }

    @Test
    void writesAllRequiredLinesInCorrectOrderAndFormat() throws Exception {
        int id = 1;
        file = Path.of("player" + id + "_output.txt");

        PlayerLogger logger = PlayerLogger.create(id);

        logger.logInitialHand(List.of(1,1,2,3));
        logger.logDraw(4, 1);
        logger.logDiscard(3, 2);
        logger.logCurrentHand(List.of(1,1,2,4));
        logger.logWin();
        logger.logFinalHand(List.of(1,1,1,1));
        logger.logExit();
        logger.close();

        assertTrue(Files.exists(file), "log file must exist");

        List<String> lines = Files.readAllLines(file);
        assertEquals(7, lines.size(), "expected 7 lines");
        assertEquals("player 1 initial hand 1 1 2 3", lines.get(0));
        assertEquals("player 1 draws a 4 from deck 1", lines.get(1));
        assertEquals("player 1 discards a 3 to deck 2", lines.get(2));
        assertEquals("player 1 current hand is 1 1 2 4", lines.get(3));
        assertEquals("player 1 wins", lines.get(4));
        assertEquals("player 1 final hand: 1 1 1 1", lines.get(5));
        assertEquals("player 1 exits", lines.get(6));
    }
}
