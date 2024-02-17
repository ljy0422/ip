package jiayou.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jiayou.function.Storage;
import jiayou.task.Event;
import org.junit.jupiter.api.Test;

public class StorageTest {
    @Test
    public void testParseFromFile() {
        Event expected = new Event("Complete homework", "2010-01-01", "2020-01-01");
        String input = "E | 0 | Complete homework | from 2010-01-01 to 2020-01-01";
        assertEquals(expected, Storage.parseFromFile(input));
    }
}
