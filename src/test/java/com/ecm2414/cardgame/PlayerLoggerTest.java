package com.ecm2414.cardgame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PlayerLoggerTest {

    @TempDir Path tmp;

    @Test
    void writesAllRequiredLinesInCorrectOrderAndFormat() throws Exception {
        String prev = System.getProperty("user.dir");
        System.setProperty("user.dir", tmp.toString());
        try (PlayerLogger log = PlayerLogger.create(1)) {
            log.logInitialHand(List.of(1, 1, 2, 3));
            log.logDraw(4, 1);
            log.logDiscard(3, 2);
            log.logCurrentHand(List.of(1, 1, 2, 4));
            log.logInformedByWinner(3);
            log.logWin();
            log.logFinalHand(List.of(1, 1, 1, 1));
            log.logExit();
        } finally {
            System.setProperty("user.dir", prev);
        }

        Path file = tmp.resolve("player1_output.txt");
        assertTrue(Files.exists(file));
        List<String> lines = Files.readAllLines(file);

        assertEquals(List.of(
                "player 1 initial hand 1 1 2 3",
                "player 1 draws a 4 from deck 1",
                "player 1 discards a 3 to deck 2",
                "player 1 current hand is 1 1 2 4",
                "player 3 has informed player 1 that player 3 has won",
                "player 1 wins",
                "player 1 final hand: 1 1 1 1",
                "player 1 exits"
        ), lines);
    }
}
