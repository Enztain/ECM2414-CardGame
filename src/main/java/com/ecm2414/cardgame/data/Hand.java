package com.ecm2414.cardgame.data;

import java.util.*;

/**
 * Represents a player's hand of cards in the card game.
 * <p>
 * A hand normally contains 4 cards, but may temporarily hold 5 during
 * a player's draw-discard turn. All methods are synchronized to ensure
 * thread-safety in a concurrent game environment.
 */
public class Hand {
    private final List<Card> cards = new ArrayList<>(4);

    /**
     * Creates a new Hand with the given initial cards.
     *
     * @param initialCards the cards to initialize the hand with
     */
    public Hand(List<Card> initialCards) {
        cards.addAll(initialCards);
    }

    /**
     * Adds a card to the hand. Can hold up to 5 cards temporarily during a turn.
     *
     * @param card the card to add
     * @throws IllegalStateException if the hand already has 5 cards
     */
    public synchronized void addCard(Card card) {
        int length = cards.size();
        if (length >= 5) {
            throw new IllegalStateException("Hand already has 4 cards");
        }
        cards.add(card);
    }

    /**
     * Removes and returns the card at the specified index.
     *
     * @param index the index of the card to remove
     * @return the removed card
     * @throws IllegalStateException if the hand is empty or index is out of range
     */
    public synchronized Card removeCard(int index) {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Hand is empty");
        }

        if (index < 0 || index >= cards.size()) {
            throw new IllegalStateException("Index is out of range");
        }

        Card removedCard = cards.get(index);
        cards.remove(index);
        return removedCard;
    }

    /**
     * Chooses a card to discard based on the player's preferred value.
     * Prefers to discard a non-preferred value. If all cards match the preferred value,
     * returns the first card.
     *
     * @param preferredValue the preferred denomination for the player
     * @return the card chosen to discard
     * @throws IllegalStateException if the hand is empty
     */
    public synchronized Card chooseDiscard(int preferredValue) {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Hand is empty, cannot choose a discard");
        }

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card.getValue() != preferredValue) {
                return card;
            }
        }
        return cards.get(0);
    }

    /**
     * Checks if this hand contains four cards of the same value.
     *
     * @return true if all four cards are equal in value, false otherwise
     */
    public synchronized boolean hasWinningHand() {
        if (cards.size() != 4) {
            return false;
        }
        int target = cards.get(0).getValue();

        for (int i = 1; i < 4; i++) {
            if (cards.get(i).getValue() != target) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a copy of the cards currently in the hand.
     *
     * @return a list containing the current cards
     */
    public synchronized List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Returns the number of cards currently in the hand.
     *
     * @return current hand size
     */
    public synchronized int size() {
        return cards.size();
    }

    /**
     * Checks if the given card is currently in the hand.
     *
     * @param card the card to check
     * @return true if the card is in the hand, false otherwise
     */
    public synchronized boolean contains(Card card) {
        return cards.contains(card);
    }

    /**
     * Clears all cards from the hand.
     */
    public synchronized void clear() {
        cards.clear();
    }

    /**
     * Returns a space-separated string of card values,
     * e.g. "1 1 2 4" for use in game logs.
     *
     * @return a string representation of the hand
     */
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i).getValue());
            if (i < cards.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a list of the card values currently in the hand.
     *
     * @return list of integer values
     */
    public synchronized List<Integer> getCardValues() {
        List<Integer> values = new ArrayList<>(cards.size());
        for (Card c : cards) {
            values.add(c.getValue());
        }
        return values;
    }

    /**
     * Replaces the current cards with the given list.
     *
     * @param newCards the new set of cards
     */
    public synchronized void setCards(List<Card> newCards) {
        cards.clear();
        cards.addAll(newCards);
    }

    /**
     * Checks if the hand currently holds exactly 4 cards.
     *
     * @return true if valid, false otherwise
     */
    public synchronized boolean isValidHand() {
        return cards.size() == 4;
    }
}
