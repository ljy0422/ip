package jiayou.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void testToStringForStore() {
        LocalDate by = LocalDate.parse("2020-01-01");
        Deadline deadline = new Deadline("Watch movies", by);
        String expected = "D | 0 | Watch movies | by2020-01-01";
        assertEquals(expected, deadline.toStringForStore());
    }

    @Test
    public void testToString() {
        LocalDate by = LocalDate.parse("2020-01-01");
        Deadline deadline = new Deadline("Watch movies", by);
        String expected = "[D][ ] Watch movies (by: Jan 1 2020)";
        assertEquals(expected, deadline.toString());
    }

    @Test
    public void testToDoDescription() {
        LocalDate by = LocalDate.parse("2020-01-01");
        String description = "Watch movies";
        Deadline deadline = new Deadline("Watch movies", by);
        assertEquals(description, deadline.getDescription());
    }

    @Test
    public void testEquals() {
        Deadline deadline = new Deadline("Watch movies", "2020-01-01");
        Deadline deadlineSame = new Deadline("Watch movies", "2020-01-01");
        Deadline deadlineDiffDesc = new Deadline("Watch", "2020-01-01");
        Deadline deadlineDiffBy = new Deadline("Watch", "2020-02-01");
        assertEquals(true, deadline.equals(deadlineSame));
        assertEquals(false, deadline.equals(deadlineDiffBy));
        assertEquals(false, deadline.equals(deadlineDiffDesc));
    }
}
