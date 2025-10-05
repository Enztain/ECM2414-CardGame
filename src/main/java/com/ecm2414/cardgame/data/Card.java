package com.ecm2414.cardgame.data;

public final class Card {
    private final int value;

    /**
     *
     * @param value passes the value of a card
     */
    public Card(int value) {
        if (value < 0) throw new IllegalArgumentException("Card value must be non-negative.");
        this.value = value;
    }

    /**
     *
     * @return returns int value of a card
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return string representation of a class
     */
    @Override
    public String toString() {
        return "Card(" + value + ")";
    }

    /**
     *
     * @param obj passes obj, pressumably card
     * @return whether the cards' values are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        return this.value == other.value;
    }

    /**
     * @return an integer hash code representing this card
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
