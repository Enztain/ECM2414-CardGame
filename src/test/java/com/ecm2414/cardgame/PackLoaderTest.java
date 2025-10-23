package com.ecm2414.cardgame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PackLoaderTest {

    @TempDir Path tmp;

    @Test
    void load_validPack_returnsUnmodifiableListOfSize8n() throws Exception {
        int n = 2;
        Path pack = tmp.resolve("pack.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8 * n; i++) sb.append(i).append('\n');
        Files.writeString(pack, sb.toString());

        List<Integer> values = PackLoader.load(pack, n);
        assertEquals(16, values.size());
        assertEquals(0, values.get(0));
        assertEquals(15, values.get(15));
        assertThrows(UnsupportedOperationException.class, () -> values.add(99));
    }

    @Test
    void load_rejectsInvalidPlayerCount() throws Exception {
        Path pack = tmp.resolve("pack.txt");
        Files.writeString(pack, "0\n0\n0\n0\n0\n0\n0\n0\n");
        assertThrows(InvalidPackException.class, () -> PackLoader.load(pack, 0));
        assertThrows(InvalidPackException.class, () -> PackLoader.load(pack, -1));
    }

    @Test
    void load_rejectsMalformedPack_badIntegerAndNegative() throws Exception {
        int n = 1;
        Path badInt = tmp.resolve("pack_bad_int.txt");
        Files.writeString(badInt, "1\n2\nX\n4\n5\n6\n7\n8\n");
        assertThrows(InvalidPackException.class, () -> PackLoader.load(badInt, n));

        Path negative = tmp.resolve("pack_negative.txt");
        Files.writeString(negative, "0\n0\n0\n0\n0\n0\n0\n-1\n");
        assertThrows(InvalidPackException.class, () -> PackLoader.load(negative, n));
    }

    @Test
    void load_rejectsWrongLineCount_andEmptyFile() throws Exception {
        int n = 2;
        Path shortFile = tmp.resolve("pack_short.txt");
        Files.writeString(shortFile, "1\n2\n3\n");
        assertThrows(InvalidPackException.class, () -> PackLoader.load(shortFile, n));

        Path empty = tmp.resolve("pack_empty.txt");
        Files.writeString(empty, "");
        assertThrows(InvalidPackException.class, () -> PackLoader.load(empty, 1));
    }
}
