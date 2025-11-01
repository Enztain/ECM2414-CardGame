package com.ecm2414.cardgame;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a thread-safe deck of cards for the card game.
 * Players draw from the top (front) and discard to the bottom (back).
 */
public class Deck implements IDeck {
    private final int id;
    private final Queue<Card> queue = new LinkedList<>();

    /**
     * Returns a copy of the cards currently in the deck.
     * Thread-safe.
     *
     * @return a list containing the current cards in the deck
     */
    public synchronized List<Card> getCards() {
        return new ArrayList<>(queue);
    }


    public Deck(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * Adds a single card to the bottom of the deck (discard pile).
     */
    @Override
    public void addCard(Card c) {
        synchronized (this) {
            queue.add(c);
            this.notifyAll(); // notify any waiting player
        }
    }

    /**
     * Adds multiple cards to the bottom of the deck.
     * Useful for initializing the deck from remaining pack cards.
     */
    public void addCards(List<Card> cardsToAdd) {
        synchronized (this) {
            queue.addAll(cardsToAdd);
            this.notifyAll();
        }
    }

    /**
     * Draws a card from the top/front of the deck.
     * Waits if the deck is empty until a card is available.
     */
    @Override
    public Card drawCard() {
        synchronized (this) {
            while (queue.isEmpty()) {
                try {
                    this.wait(); // wait until notified by addCard
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null; // exit if interrupted
                }
            }
            return queue.poll();
        }
    }

    /**
     * Returns a string of the current card values in the deck.
     */
    @Override
    public String contentsString() {
        synchronized (this) {
            List<String> vals = new ArrayList<>();
            for (Card c : queue) {
                vals.add(String.valueOf(c.getValue()));
            }
            return String.join(" ", vals);
        }
    }

    /**
     * Writes the final contents of this deck to a file named deckX_output.txt.
     */
    public void logDeckContents() {
        String fileName = "deck" + id + "_output.txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName))) {
            writer.write("deck" + id + " contents: " + contentsString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the deck with 4 cards of this deck's ID.
     */
    public void initializeDeck() {
        synchronized (this) {
            queue.clear();
            for (int i = 0; i < 4; i++) {
                queue.add(new Card(id));
            }
            this.notifyAll();
        }
    }

    /**
     * Deals a number of cards from the top of the deck.
     * Used to give initial hands to players.
     *
     * @param count number of cards to deal
     * @return list of cards dealt (may be fewer if deck has insufficient cards)
     */
    public List<Card> dealInitialCards(int count) {
        List<Card> handCards = new ArrayList<>();
        synchronized (this) {
            for (int i = 0; i < count; i++) {
                Card c = queue.poll();
                if (c != null) handCards.add(c);
                else break;
            }
        }
        return handCards;
    }

    /**
     * Returns the current number of cards in the deck.
     */
    public int size() {
        synchronized (this) {
            return queue.size();
        }
    }
}
