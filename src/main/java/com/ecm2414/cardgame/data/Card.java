package com.ecm2414.cardgame.data;

public final class Card {
    private final int value;

    public Card(int value) {
        if (value < 0) throw new IllegalArgumentException("Card value must be non-negative.");
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Card(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
