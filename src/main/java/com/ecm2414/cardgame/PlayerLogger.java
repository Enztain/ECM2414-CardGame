package com.ecm2414.cardgame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * Writes the required per-player log file (player<i>_output.txt).
 */
public final class PlayerLogger implements AutoCloseable {

    private final int playerId;
    private final BufferedWriter writer;
    private final Object lock = new Object();

    private PlayerLogger(int playerId, BufferedWriter writer) {
        this.playerId = playerId;
        this.writer = writer;
    }

    /**
     * Creates a PlayerLogger for the given player id.
     * The output file is named "player<id>_output.txt" and will be overwritten if present.
     */
    public static PlayerLogger create(int playerId) throws IOException {
        String fileName = "player" + playerId + "_output.txt";
        Path path = Path.of(fileName);
        BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        return new PlayerLogger(playerId, w);
    }

    /** Logs the initial hand dealt to the player. */
    public void logInitialHand(Collection<Integer> hand) throws IOException {
        writeLine("player " + playerId + " initial hand " + join(hand));
    }

    /** Logs that the player drew a card from a specific deck. */
    public void logDraw(int cardValue, int fromDeckId) throws IOException {
        writeLine("player " + playerId + " draws a " + cardValue + " from deck " + fromDeckId);
    }

    /** Logs that the player discarded a card to a specific deck. */
    public void logDiscard(int cardValue, int toDeckId) throws IOException {
        writeLine("player " + playerId + " discards a " + cardValue + " to deck " + toDeckId);
    }

    /** Logs the player's current hand after an action. */
    public void logCurrentHand(Collection<Integer> hand) throws IOException {
        writeLine("player " + playerId + " current hand is " + join(hand));
    }

    /** Logs that another player has informed this player that they have won. */
    public void logInformedByWinner(int winnerId) throws IOException {
        writeLine("player " + winnerId + " has informed player " + playerId + " that player " + winnerId + " has won");
    }

    /** Logs that this player has won. */
    public void logWin() throws IOException {
        writeLine("player " + playerId + " wins");
    }

    /** Logs the player's final hand at game end. */
    public void logFinalHand(Collection<Integer> hand) throws IOException {
        writeLine("player " + playerId + " final hand: " + join(hand));
    }

    /** Logs that this player exits. */
    public void logExit() throws IOException {
        writeLine("player " + playerId + " exits");
    }

    /** Close the underlying writer. Call this in a finally block. */
    @Override
    public void close() throws IOException {
        synchronized (lock) {
            writer.close();
        }
    }


    private void writeLine(String s) throws IOException {
        synchronized (lock) {
            writer.write(s);
            writer.newLine();
            writer.flush();
        }
    }

    private static String join(Collection<Integer> ints) {
        StringJoiner sj = new StringJoiner(" ");
        for (Integer i : ints) sj.add(String.valueOf(i));
        return sj.toString();
    }
}
