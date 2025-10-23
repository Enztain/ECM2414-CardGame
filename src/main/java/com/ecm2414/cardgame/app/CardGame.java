package com.ecm2414.cardgame.app;
import com.ecm2414.cardgame.Card;

public class CardGame {
    public static void main(String[] args) {
        System.out.println("Card game starting…");
        Card c1 = new Card(5);
        Card c2 = new Card(5);
        System.out.println(c1.equals(c2)); // should print true
        System.out.println(c1); // should print Card(5)
    }
}


