package com.ecm2414.cardgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GameController sets up and runs the multithreaded card game.
 * It relies on GameConfig to provide the pack and initial hands.
 */
public class GameController {

    private final AtomicBoolean winnerFound = new AtomicBoolean(false);

    /** Starts a new game from the provided configuration. */
    public void startGame(GameConfig config) {
        final int n = config.getNumPlayers();

        // Create decks (ids are 1..n for logging/filenames).
        Deck[] decks = new Deck[n];
        for (int i = 0; i < n; i++) {
            decks[i] = new Deck(i + 1);
        }

        // Deal initial hands and fill decks with the remaining cards.
        List<Hand> hands = config.dealInitialHands();
        config.fillDecks(decks);

        // Check for immediate winner after the initial deal.
        int immediateWinner = findImmediateWinner(hands);
        if (immediateWinner != -1) {
            System.out.println("player " + immediateWinner + " wins");
            emitInitialDealLogsAndFinish(immediateWinner, hands, decks);
            return;
        }

        // Create player objects & threads.
        List<Thread> threads = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int playerId = i + 1;
            Deck left = decks[i];
            Deck right = decks[(i + 1) % n];
            Player p = new Player(playerId, hands.get(i), left, right, winnerFound);
            Thread t = new Thread(p, "player-" + playerId);
            threads.add(t);
        }

        // Start all players.
        for (Thread t : threads) t.start();

        // Wait for termination.
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // try to finish gracefully
            }
        }

        // Emit deck logs at the end of the game.
        writeDeckLogs(decks);
    }

    /** Returns 1-based player id of a winner if any, otherwise -1. */
    private int findImmediateWinner(List<Hand> hands) {
        for (int i = 0; i < hands.size(); i++) {
            if (isWinningHand(hands.get(i))) {
                winnerFound.set(true);
                return i + 1;
            }
        }
        return -1;
    }

    /** A winning hand is four cards of the same value. */
    private boolean isWinningHand(Hand hand) {
        List<Integer> values = hand.getCardValues();
        if (values.size() != 4) return false;
        int v0 = values.get(0);
        return values.get(1) == v0 && values.get(2) == v0 && values.get(3) == v0;
    }

    /** If someone wins on the initial deal, create the required logs and exit. */
    private void emitInitialDealLogsAndFinish(int winnerId, List<Hand> hands, Deck[] decks) {
        // Per spec, create all player output files even on immediate victory.
        for (int i = 0; i < hands.size(); i++) {
            int playerId = i + 1;
            PlayerLogger logger = null;
            try {
                logger = PlayerLogger.create(playerId);
                logger.logInitialHand(hands.get(i).getCardValues());
                if (playerId == winnerId) {
                    logger.logWin();
                    logger.logFinalHand(hands.get(i).getCardValues());
                } else {
                    logger.logInformedByWinner(winnerId);
                    logger.logExit();
                    logger.logFinalHand(hands.get(i).getCardValues());
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (logger != null) {
                    try { logger.close(); } catch (IOException ignored) {}
                }
            }
        }
        writeDeckLogs(decks);
    }

    /** Writes deckX_output.txt files for each deck. */
    private void writeDeckLogs(Deck[] decks) {
        for (Deck d : decks) {
            // соберём значения карт по порядку (как лежат в очереди)
            List<Integer> values = new java.util.ArrayList<>();
            // если у Deck есть метод snapshot/iterator/getCards — используй его;
            // иначе добавь в Deck публичный метод, который вернёт копию содержимого.
            for (Card c : d.getCards()) {
                values.add(c.getValue());
            }
            try {
                DeckLogger.logFinalState(d.getId(), values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
