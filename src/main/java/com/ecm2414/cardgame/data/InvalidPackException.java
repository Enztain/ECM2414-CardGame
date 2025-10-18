package com.ecm2414.cardgame.data;

/** Exception thrown when a pack file is invalid. */
public class InvalidPackException extends Exception {
    public InvalidPackException(String message) {
        super(message);
    }
}
