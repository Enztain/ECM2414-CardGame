package com.ecm2414.cardgame;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores configuration and setup parameters for a card game.
 * Used by GameController to initialize decks and players.
 */
public class GameConfig {

    private final int numPlayers;        // number of players in the game
    private final Path packFilePath;     // location of input pack file
    private final List<Card> pack;       // list of cards read from file

    /**
     * Constructs a GameConfig.
     *
     * @param numPlayers number of players (must be > 0)
     * @param packFilePath path to input pack file
     * @param pack list of cards from the pack (must contain 8*numPlayers cards)
     */
    public GameConfig(int numPlayers, Path packFilePath, List<Card> pack) {
        if (numPlayers <= 0) {
            throw new IllegalArgumentException("Number of players must be positive.");
        }
        if (pack == null || pack.size() != numPlayers * 8) {
            throw new IllegalArgumentException("Pack must contain exactly 8 * numPlayers cards.");
        }
        this.numPlayers = numPlayers;
        this.packFilePath = packFilePath;
        this.pack = new ArrayList<>(pack); // defensive copy
    }

    /** Returns the number of players in the game */
    public int getNumPlayers() {
        return numPlayers;
    }

    /** Returns the input pack file path */
    public Path getPackFilePath() {
        return packFilePath;
    }

    /** Returns a copy of the pack cards */
    public List<Card> getPack() {
        return new ArrayList<>(pack);
    }

    /**
     * Deals the initial hands for all players round-robin.
     *
     * @return a list of hands, one per player
     */
    public List<Hand> dealInitialHands() {
        List<Hand> hands = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            hands.add(new Hand(new ArrayList<>()));
        }

        int index = 0;
        for (int round = 0; round < 4; round++) { // 4 cards per hand
            for (int p = 0; p < numPlayers; p++) {
                hands.get(p).addCard(pack.get(index++));
            }
        }

        return hands;
    }

    /**
     * Fills the decks with remaining cards round-robin.
     *
     * @param decks array of deck objects to populate
     */
    public void fillDecks(Deck[] decks) {
        int index = numPlayers * 4; // skip cards dealt to players
        while (index < pack.size()) {
            for (int d = 0; d < decks.length && index < pack.size(); d++) {
                decks[d].addCard(pack.get(index++));
            }
        }
    }
}
