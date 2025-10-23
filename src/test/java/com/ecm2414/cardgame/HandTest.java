package com.ecm2414.cardgame;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    private static Card c(int v) { return new Card(v); }

    @Test
    void constructor_initialisesWithGivenCards() {
        Hand h = new Hand(List.of(c(1), c(2), c(3), c(4)));
        assertEquals(4, h.size());
        assertEquals(List.of(1,2,3,4), h.getCardValues());
        assertTrue(h.isValidHand());
    }

    @Test
    void addCard_allowsUpToFive_thenThrowsOnSixth() {
        Hand h = new Hand(List.of(c(1), c(2), c(3), c(4)));
        h.addCard(c(9));
        assertEquals(5, h.size());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> h.addCard(c(10)));
        assertTrue(ex.getMessage().toLowerCase().contains("hand"), "error message should mention hand being full");
    }

    @Test
    void removeCard_removesByIndex_andChecksBounds() {
        Hand h = new Hand(List.of(c(5), c(6), c(7), c(8)));
        Card removed = h.removeCard(1);
        assertEquals(6, removed.getValue());
        assertEquals(List.of(5,7,8), h.getCardValues());

        assertThrows(IllegalStateException.class, () -> h.removeCard(-1));
        assertThrows(IllegalStateException.class, () -> h.removeCard(99));
    }

    @Test
    void chooseDiscard_prefersNonPreferredValue_whenAvailable() {
        Hand h = new Hand(List.of(c(1), c(2), c(3), c(1)));
        int preferred = 1;
        Card toDiscard = h.chooseDiscard(preferred);
        assertNotEquals(preferred, toDiscard.getValue(), "Should discard a non-preferred value when possible");
    }

    @Test
    void chooseDiscard_returnsFirst_whenAllCardsPreferred() {
        Hand h = new Hand(List.of(c(4), c(4), c(4), c(4)));
        Card toDiscard = h.chooseDiscard(4);
        assertEquals(4, toDiscard.getValue());
    }

    @Test
    void hasWinningHand_trueWhenExactlyFourEqual() {
        Hand h = new Hand(List.of(c(7), c(7), c(7), c(7)));
        assertTrue(h.hasWinningHand());
    }

    @Test
    void hasWinningHand_falseWhenNotExactlyFourOrMixed() {
        Hand h1 = new Hand(List.of(c(1), c(1), c(1)));
        assertFalse(h1.hasWinningHand());

        Hand h2 = new Hand(List.of(c(1), c(1), c(2), c(3)));
        assertFalse(h2.hasWinningHand());
    }

    @Test
    void toString_listsValuesSpaceSeparated() {
        Hand h = new Hand(List.of(c(1), c(1), c(2), c(4)));
        assertEquals("1 1 2 4", h.toString());
    }

    @Test
    void getCardValues_returnsListOfIntegers() {
        Hand h = new Hand(List.of(c(9), c(9), c(9), c(9)));
        assertEquals(List.of(9,9,9,9), h.getCardValues());
    }

    @Test
    void isValidHand_trueOnlyWhenSizeIsFour() {
        Hand h = new Hand(List.of(c(1), c(2), c(3), c(4)));
        assertTrue(h.isValidHand());

        h.addCard(c(5));
        assertFalse(h.isValidHand());

        h.removeCard(0);
        assertTrue(h.isValidHand());
    }


    @Test
    void contains_andClear_behaveAsExpected() {
        Hand h = new Hand(List.of(c(2), c(3)));
        Card three = h.getCards().get(1);
        assertTrue(h.contains(three));
        h.clear();
        assertEquals(0, h.size());
        assertFalse(h.contains(three));
    }
}
