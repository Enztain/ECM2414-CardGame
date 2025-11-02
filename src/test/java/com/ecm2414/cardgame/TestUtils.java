package com.ecm2414.cardgame;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class TestUtils {
    private TestUtils() {}

    static List<Card> cards(int... values) {
        List<Card> list = new ArrayList<>();
        for (int v : values) list.add(new Card(v));
        return list;
    }

    static Hand hand(int... values) {
        return new Hand(cards(values));
    }

    static Deck deck(int id, int... values) {
        Deck d = new Deck(id);
        d.addCards(cards(values)); // если у твоего Deck нет addCards(List<Card>), замени на цикл d.addCard(...)
        return d;
    }

    static Path writePackFile(List<Integer> values) throws Exception {
        Path f = Files.createTempFile("pack", ".txt");
        StringBuilder sb = new StringBuilder();
        for (Integer v : values) sb.append(v).append('\n');
        Files.writeString(f, sb.toString());
        return f;
    }
}
