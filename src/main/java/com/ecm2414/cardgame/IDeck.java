package com.ecm2414.cardgame;

public interface IDeck {
    int getId();
    void addCard(Card c);   // discard to bottom
    Card drawCard();        // draw from top
    String contentsString(); // final contents for logger
}