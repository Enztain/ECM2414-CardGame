package com.ecm2414.cardgame;


import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class GameControllerIntegrationTest {
    @Test
    void startGameCreatesOutputsAndStops() throws Exception {
        int n = 2;
        List<Integer> values = new ArrayList<>();
        values.addAll(List.of(1,1,1,1));
        values.addAll(List.of(2,3,4,5));
        values.addAll(List.of(9,9,9,9,7,7,7,7));


        Path packPath = TestUtils.writePackFile(values);
        List<Card> packCards = new ArrayList<>();
        for (int v : values) packCards.add(new Card(v));


        GameConfig cfg = new GameConfig(n, packPath, packCards);
        GameController controller = new GameController();
        controller.startGame(cfg);


        assertTrue(java.nio.file.Files.exists(Path.of("player1_output.txt")));
    }
}