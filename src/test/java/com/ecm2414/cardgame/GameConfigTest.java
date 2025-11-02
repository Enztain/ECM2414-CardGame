package com.ecm2414.cardgame;


import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class GameConfigTest {
    @Test
    void holdsPlayersPackAndDealsHands() {
        List<Card> pack = TestUtils.cards(1,2,3,4, 5,6,7,8);
        GameConfig cfg = new GameConfig(1, Path.of("pack.txt"), pack);
        assertEquals(1, cfg.getNumPlayers());
    }
}