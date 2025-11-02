package com.ecm2414.cardgame;


import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;


class PlayerConcurrencyTest {
    @Test
    void twoPlayersRaceToWinStopsBoth() throws Exception {
        Deck deck1 = TestUtils.deck(1, 1, 1, 1, 1);
        Deck deck2 = TestUtils.deck(2, 2, 3, 4, 5);


        Hand h1 = TestUtils.hand(1,1,2,3);
        Hand h2 = TestUtils.hand(2,2,2,3);


        AtomicBoolean winnerFound = new AtomicBoolean(false);
        Player p1 = new Player(1, h1, deck1, deck2, winnerFound);
        Player p2 = new Player(2, h2, deck2, deck1, winnerFound);


        ExecutorService ex = Executors.newFixedThreadPool(2);
        var f1 = ex.submit(() -> { p1.run(); return p1.hasWon(); });
        var f2 = ex.submit(() -> { p2.run(); return p2.hasWon(); });


        boolean w1 = f1.get(3, TimeUnit.SECONDS);
        boolean w2 = f2.get(3, TimeUnit.SECONDS);
        ex.shutdownNow();


        assertTrue(w1 || w2);
        assertTrue(winnerFound.get());
    }
}