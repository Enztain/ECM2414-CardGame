package com.ecm2414.cardgame;


import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class DeckTest {
    @Test
    void fifoAddDrawAndSize() {
        Deck d = new Deck(1);
        d.addCards(TestUtils.cards(10, 11, 12));
        assertEquals(3, d.size());
        assertEquals(10, d.drawCard().getValue());
        assertEquals(11, d.drawCard().getValue());
        d.addCard(new Card(99));
        assertEquals(12, d.drawCard().getValue());
        assertEquals(99, d.drawCard().getValue());
        assertEquals(0, d.size());
    }


    @Test
    void contentsStringReflectsState() {
        Deck d = TestUtils.deck(2, 3, 3, 7);
        assertTrue(d.contentsString().contains("3"));
    }
}