package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SetStatusCommandParserTest {

    private final SetStatusCommandParser parser = new SetStatusCommandParser();

    @Test
    public void parse_validIndexOnly_parsesWithBlankStatus() throws Exception {
        SetStatusCommand cmd = parser.parse("1");
        assertEquals(new SetStatusCommand(Index.fromOneBased(1), ""), cmd);
    }

    @Test
    public void parse_validIndexAndStatus_parses() throws Exception {
        SetStatusCommand cmd = parser.parse("2 Contacted");
        assertEquals(new SetStatusCommand(Index.fromOneBased(2), "Contacted"), cmd);
    }

    @Test
    public void parse_whitespaceAround_parses() throws Exception {
        SetStatusCommand cmd = parser.parse(PREAMBLE_WHITESPACE + "3   rejected   ");
        assertEquals(new SetStatusCommand(Index.fromOneBased(3), "rejected"), cmd);
    }

    @Test
    public void parse_empty_throws() {
        assertThrows(ParseException.class, () -> parser.parse("      "));
    }

    @Test
    public void parse_invalidIndex_throws() {
        assertThrows(ParseException.class, () -> parser.parse("zero Contacted"));
        assertThrows(ParseException.class, () -> parser.parse("-1 Contacted"));
    }
}


