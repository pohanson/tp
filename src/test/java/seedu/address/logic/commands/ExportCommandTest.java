package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.clipboard.ClipboardProvider;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ExportCommandTest {

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

    private static class FakeFileSystem implements seedu.address.logic.files.FileSystemProvider {
        Path expectedPath;
        String fileContent;
        boolean exists = true;

        @Override
        public boolean exists(Path path) {
            return exists;
        }

        @Override
        public String readFile(Path path) {
            return fileContent;
        }
    }

    @Test
    public void execute_fileMissing_throws() {
        Model model = new ModelManager();
        model.setUserPrefs(new UserPrefs());
        model.setAddressBookFilePath(Path.of("fakefile.json"));

        FakeFileSystem fs = new FakeFileSystem();
        fs.exists = false;
        ExportCommand cmd = new ExportCommand(new FakeClipboard(), fs);
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_validFileCopiesToClipboard() throws Exception {
        Model model = new ModelManager();
        model.setUserPrefs(new UserPrefs());
        model.setAddressBookFilePath(Path.of("addressbook.json"));

        String json = "{\n  \"persons\": []\n}";
        FakeFileSystem fs = new FakeFileSystem();
        fs.exists = true;
        fs.fileContent = json;

        FakeClipboard fake = new FakeClipboard();
        ExportCommand cmd = new ExportCommand(fake, fs);
        CommandResult result = cmd.execute(model);

        assertEquals("Copied address book data to clipboard.", result.getFeedbackToUser());
        assertEquals(json, fake.getString());
    }
}


