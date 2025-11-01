package com.ecm2414.cardgame;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Runnable, IPlayer {
    private final int id;
    private final Hand hand;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final AtomicBoolean winnerFound;
    private volatile boolean IWon = false;
    private static volatile int winnerId = -1; // Shared winner ID across all players

    public Player(int id, Hand hand, Deck leftDeck, Deck rightDeck, AtomicBoolean winnerFound) {
        this.id = id;
        this.hand = hand;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.winnerFound = winnerFound;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean hasWon() {
        return IWon;
    }

    private int preferredValue() {
        return id; // Player i prefers cards with value i
    }

    @Override
    public void run() {
        PlayerLogger logger = null;
        try {
            // Initialize logger
            logger = PlayerLogger.create(id);
            logger.logInitialHand(hand.getCardValues());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Check if player wins immediately at start
        if (hand.hasWinningHand()) {
            IWon = true;
            winnerFound.set(true);
            winnerId = id;
            System.out.println("Player " + id + " wins");
            try {
                logger.logWin();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeLogger(logger);
            return;
        }

        // Main game loop
        while (!winnerFound.get()) {
            // Lock decks in consistent order to avoid deadlock
            Deck firstLock = (leftDeck.getId() < rightDeck.getId()) ? leftDeck : rightDeck;
            Deck secondLock = (firstLock == leftDeck) ? rightDeck : leftDeck;

            synchronized (firstLock) {
                synchronized (secondLock) {
                    if (winnerFound.get()) break;

                    // Draw a card from left deck
                    Card drawn = leftDeck.drawCard();
                    if (drawn != null) {
                        hand.addCard(drawn);

                        // Choose a card to discard
                        Card discard = hand.chooseDiscard(preferredValue());
                        // Remove discard from hand safely
                        synchronized (hand) {
                            if (hand.getCards().contains(discard)) {
                                hand.removeCard(hand.getCards().indexOf(discard));
                            } else {
                                discard = drawn; // fallback
                                hand.removeCard(hand.getCards().indexOf(discard));
                            }
                        }
                        // Add discard to right deck
                        rightDeck.addCard(discard);

                        // Log actions
                        try {
                            logger.logDraw(drawn.getValue(), leftDeck.getId());
                            logger.logDiscard(discard.getValue(), rightDeck.getId());
                            logger.logCurrentHand(hand.getCardValues());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Check if this hand is winning
                        if (hand.hasWinningHand()) {
                            IWon = true;
                            winnerFound.set(true);
                            winnerId = id;
                            System.out.println("Player " + id + " wins");
                            try {
                                logger.logWin();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                } // release secondLock
            } // release firstLock

            // Avoid busy loop
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }

        // Notify if another player has won
        if (!IWon && winnerFound.get() && winnerId != -1) {
            try {
                logger.logInformedByWinner(winnerId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Log player exit
        try {
            logger.logExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeLogger(logger);
    }

    /** Safely closes logger */
    private void closeLogger(PlayerLogger logger) {
        if (logger != null) {
            try {
                logger.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
