package com.ecm2414.cardgame;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackValidatorTest {

    @Test
    void validatePlayerCount_rejectsZeroOrNegative() {
        assertThrows(InvalidPackException.class, () -> PackValidator.validatePlayerCount(0));
        assertThrows(InvalidPackException.class, () -> PackValidator.validatePlayerCount(-3));
    }

    @Test
    void validatePlayerCount_acceptsPositive() {
        assertDoesNotThrow(() -> PackValidator.validatePlayerCount(1));
        assertDoesNotThrow(() -> PackValidator.validatePlayerCount(5));
    }

    @Test
    void validateLineCount_acceptsExactly8n() throws Exception {
        int n = 3;
        List<Integer> lines = Collections.nCopies(8 * n, 0);
        PackValidator.validateLineCount(lines, n);
    }

    @Test
    void validateLineCount_rejectsEmptyAndWrongCount() {
        int n = 2;

        InvalidPackException empty = assertThrows(
                InvalidPackException.class,
                () -> PackValidator.validateLineCount(List.of(), n)
        );
        assertTrue(empty.getMessage().toLowerCase().contains("empty"));

        InvalidPackException wrong = assertThrows(
                InvalidPackException.class,
                () -> PackValidator.validateLineCount(List.of(1, 2, 3), n)
        );
        assertTrue(wrong.getMessage().toLowerCase().contains("expected"));
    }

    @Test
    void validateLineCount_worksWithMutableListsToo() {
        int n = 1;
        List<Integer> lines = new ArrayList<>();
        for (int i = 0; i < 8; i++) lines.add(0);
        assertDoesNotThrow(() -> PackValidator.validateLineCount(lines, n));
    }

    @Test
    void parseNonNegativeInt_acceptsValid_andWhitespace_andMaxInt() throws Exception {
        assertEquals(0, PackValidator.parseNonNegativeInt("0", 1));
        assertEquals(42, PackValidator.parseNonNegativeInt("   42  ", 2));
        assertEquals(Integer.MAX_VALUE,
                PackValidator.parseNonNegativeInt(String.valueOf(Integer.MAX_VALUE), 3));
    }

    @Test
    void parseNonNegativeInt_rejectsBlank_showsLineNumber() {
        InvalidPackException e = assertThrows(
                InvalidPackException.class,
                () -> PackValidator.parseNonNegativeInt("   ", 7)
        );
        assertTrue(e.getMessage().contains("Line 7"));
        assertTrue(e.getMessage().toLowerCase().contains("blank"));
    }

    @Test
    void parseNonNegativeInt_rejectsNegative_showsLineNumber() {
        InvalidPackException e = assertThrows(
                InvalidPackException.class,
                () -> PackValidator.parseNonNegativeInt("-1", 4)
        );
        assertTrue(e.getMessage().contains("Line 4"));
        assertTrue(e.getMessage().toLowerCase().contains("negative"));
    }

    @Test
    void parseNonNegativeInt_rejectsNonInteger_showsLineNumber() {
        InvalidPackException e = assertThrows(
                InvalidPackException.class,
                () -> PackValidator.parseNonNegativeInt("x9", 9)
        );
        assertTrue(e.getMessage().contains("Line 9"));
        assertTrue(e.getMessage().toLowerCase().contains("valid integer"));
    }
}
