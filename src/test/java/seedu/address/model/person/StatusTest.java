package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Status} value handling and validation.
 */
public class StatusTest {

    @Test
    public void isValidStatus_validValues_caseInsensitive() {
        assertTrue(Status.isValidStatus("Uncontacted"));
        assertTrue(Status.isValidStatus("uncontacted"));
        assertTrue(Status.isValidStatus("CONTACTED"));
        assertTrue(Status.isValidStatus("RejecteD"));
        assertTrue(Status.isValidStatus("aCcEpTeD"));
        assertTrue(Status.isValidStatus("UnrEAchAbLe"));
        assertTrue(Status.isValidStatus("   buSy   "));
    }

    @Test
    public void isValidStatus_invalidValues() {
        assertFalse(Status.isValidStatus(""));
        assertFalse(Status.isValidStatus("         "));
        assertFalse(Status.isValidStatus("friend"));
    }

    @Test
    public void fromStringIgnoreCase_mapsCorrectly_andTrims() {
        assertEquals(Status.UNCONTACTED, Status.fromStringIgnoreCase(" Uncontacted "));
        assertEquals(Status.CONTACTED, Status.fromStringIgnoreCase("contacted"));
        assertEquals(Status.REJECTED, Status.fromStringIgnoreCase("REJECTED"));
        assertEquals(Status.ACCEPTED, Status.fromStringIgnoreCase("aCCeptEd"));
        assertEquals(Status.UNREACHABLE, Status.fromStringIgnoreCase("unreachable"));
        assertEquals(Status.BUSY, Status.fromStringIgnoreCase("BusY  "));
    }

    @Test
    public void fromStringIgnoreCase_invalid_throws() {
        assertThrows(IllegalArgumentException.class, () -> Status.fromStringIgnoreCase("invalid"));
        assertThrows(IllegalArgumentException.class, () -> Status.fromStringIgnoreCase(""));
        assertThrows(IllegalArgumentException.class, () -> Status.fromStringIgnoreCase("       "));
    }
}


