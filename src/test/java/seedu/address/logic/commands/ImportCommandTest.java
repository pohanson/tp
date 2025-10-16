package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.clipboard.ClipboardProvider;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ImportCommandTest {

    @Test
    public void execute_emptyClipboard_throws() {
        Model model = new ModelManager();
        ImportCommand cmd = new ImportCommand(new FakeClipboard());
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_validJsonImports() throws Exception {
        Model model = new ModelManager();
        FakeClipboard fake = new FakeClipboard();
        fake.setString("{\n  \"persons\": []\n}");
        ImportCommand cmd = new ImportCommand(fake);
        CommandResult result = cmd.execute(model);
        assertEquals("Imported address book data from clipboard.", result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidJson_throws() {
        Model model = new ModelManager();
        FakeClipboard fake = new FakeClipboard();

        fake.setString("{ WAWAWEEWA ::: }");
        ImportCommand cmd = new ImportCommand(fake);
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    private static class FakeClipboard implements ClipboardProvider {
        String value;

        @Override
        public String getString() {
            return value;
        }

        @Override
        public void setString(String v) {
            value = v;
        }
    }
}


