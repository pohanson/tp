package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/**
 * GUI tests for ImportWindow using TestFX.
 */
public class ImportWindowTest extends ApplicationTest {

    private ImportWindow importWindow;
    private Logic logic;


    @Override
    public void start(Stage stage) throws Exception {
        // Set up test dependencies
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(
                java.nio.file.Paths.get("test", "data", "temp.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(
                java.nio.file.Paths.get("test", "data", "tempPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Model model = new ModelManager();
        logic = new LogicManager(model, storage);

        // Create the ImportWindow
        importWindow = new ImportWindow(stage, logic);
        stage.show();
        stage.toFront();
    }

    @Test
    public void display_initialText_showsDefaultPreview() {
        Text preview = lookup("#jsonPreview").query();
        assertEquals("No data yet.\nPaste JSON data here", preview.getText());
    }
}
