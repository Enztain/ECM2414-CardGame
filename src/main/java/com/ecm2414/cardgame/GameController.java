package com.ecm2414.cardgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GameController sets up and runs the multithreaded card game.
 */
public class GameController {

    private final AtomicBoolean winnerFound = new AtomicBoolean(false);

    public GameController() {
    }

    /**
     * Starts the card game using the given configuration.
     *
     * @param config the game setup and pack information
     */
    public void startGame(GameConfig config) {
        int numPlayers = config.getNumPlayers();

        // Create decks
        Deck[] decks = new Deck[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            decks[i] = new Deck(i); // IDs start at 0
        }

        // Deal hands
        List<Hand> hands = config.dealInitialHands();

        // Fill decks with remaining cards
        config.fillDecks(decks);

        // Create Player objects
        Player[] players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            Deck leftDeck = decks[i];                   // draw from deck i
            Deck rightDeck = decks[(i + 1) % numPlayers]; // discard to next deck
            players[i] = new Player(i + 1, hands.get(i), leftDeck, rightDeck, winnerFound);
        }

        // Start player threads
        Thread[] threads = new Thread[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            threads[i] = new Thread(players[i], "Player-" + (i + 1));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Log final deck contents
        for (Deck d : decks) {
            try {
                List<Integer> cardValues = d.getCards().stream()
                        .map(Card::getValue)
                        .toList();
                DeckLogger.logFinalState(d.getId() + 1, cardValues); // deck IDs start at 1
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println("=== Game Over ===");
    }

    /**
     * Main method: prompts for number of players and pack file, then starts the game.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Ask number of players
        int n = 0;
        while (n <= 0) {
            System.out.print("Enter number of players: ");
            if (sc.hasNextInt()) {
                n = sc.nextInt();
            } else {
                sc.next(); // discard invalid input
            }
        }
        sc.nextLine();

        // Ask pack file path
        Path packPath = null;
        while (packPath == null) {
            System.out.print("Enter path to pack file: ");
            String pathStr = sc.nextLine();
            Path p = Path.of(pathStr);
            if (Files.exists(p) && Files.isReadable(p)) {
                packPath = p;
            } else {
                System.out.println("Invalid file path, try again.");
            }
        }

        // Read pack file
        List<Card> pack = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(packPath);
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    int val = Integer.parseInt(line);
                    pack.add(new Card(val));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Failed to read pack file: " + e.getMessage());
            return;
        }

        // Validate pack size
        if (pack.size() != 8 * n) {
            System.out.println("Invalid pack file. Must contain exactly 8n cards.");
            return;
        }

        // Create GameConfig
        GameConfig config = new GameConfig(n, packPath, pack);

        // Start the game
        GameController controller = new GameController();
        controller.startGame(config);
    }
}
