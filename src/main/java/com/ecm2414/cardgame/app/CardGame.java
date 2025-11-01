package com.ecm2414.cardgame;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class CardGame {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n = 0;
        while (true) {
            System.out.print("Please enter the number of players: ");
            String s = in.nextLine().trim();
            try {
                n = Integer.parseInt(s);
                PackValidator.validatePlayerCount(n);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            } catch (InvalidPackException e) {
                System.out.println(e.getMessage());
            }
        }

        Path packPath;
        List<Integer> ints;
        while (true) {
            System.out.print("Please enter the location of the pack to load: ");
            String pathStr = in.nextLine().trim();
            packPath = Path.of(pathStr);
            try {
                ints = PackLoader.load(packPath, n); // 8n неотрицательных
                break; // валидно — выходим из цикла
            } catch (InvalidPackException e) {
                System.out.println("Invalid pack file: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error reading pack file: " + e.getMessage());
            }
        }

        // Конвертация Integer → Card (GameConfig ожидает List<Card>)
        List<Card> pack = new ArrayList<>(ints.size());
        for (int v : ints) {
            pack.add(new Card(v));
        }

        // Собираем конфиг и стартуем игру
        GameConfig config = new GameConfig(n, packPath, pack);
        GameController controller = new GameController();
        controller.startGame(config);
    }
}
