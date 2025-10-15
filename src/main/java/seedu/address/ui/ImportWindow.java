package seedu.address.ui;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;

/**
 * Controller for the import data page
 */
public class ImportWindow extends UiPart<Stage> {
    private static final Logger logger = LogsCenter.getLogger(ImportWindow.class);
    private static final String FXML_FILE = "ImportWindow.fxml";
    private static final String DEFAULT_PREVIEW_TEXT = "No data yet.\nPaste JSON data here";

    private final Logic logic;

    @FXML
    private Text jsonPreview;

    /**
     * Creates a new ImportWindow.
     *
     * @param root  Stage to use as the root of the ImportWindow.
     * @param logic Logic instance to handle saving data.
     */
    public ImportWindow(Stage root, Logic logic) {
        super(FXML_FILE, root);
        this.logic = logic;
        jsonPreview.setText(DEFAULT_PREVIEW_TEXT);
        setupKeyboardShortcuts();
    }

    /**
     * Creates a new ImportWindow.
     */
    public ImportWindow(Logic logic) {
        this(new Stage(), logic);
    }

    /**
     * Sets up keyboard shortcuts for the import window.
     * Ctrl+V: Paste JSON from clipboard
     * Ctrl+S: Save and import data
     */
    private void setupKeyboardShortcuts() {
        KeyCombination pasteKeyCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
        KeyCombination saveKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (pasteKeyCombination.match(event)) {
                pasteJson();
                event.consume();
            } else if (saveKeyCombination.match(event)) {
                saveJson();
                event.consume();
            }
        });
    }

    /**
     * Shows the import window.
     *
     * @throws IllegalStateException <ul>
     *                               <li>
     *                               if this method is called on a thread other than the JavaFX
     *                               Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing import window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the import window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the import window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the import window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Pastes JSON data from the system clipboard.
     */
    @FXML
    public void pasteJson() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            jsonPreview.setText(clipboard.getString());
        } else {
            ShowAlert.showAlertDialogAndWait(getRoot(), AlertType.WARNING, "Error", "Error pasting",
                    "No valid text found in clipboard.");
        }
    }

    /**
     * Saves the JSON text from the preview to the address book.
     * Validates the JSON and imports it through the Logic layer.
     */
    @FXML
    public void saveJson() {
        String text = jsonPreview.getText();

        if (text == null || text.trim().isEmpty() || text.equals(DEFAULT_PREVIEW_TEXT)) {
            ShowAlert.showAlertDialogAndWait(getRoot(), AlertType.WARNING, "No Data", "Error:",
                    "Please paste JSON data before saving.");
            return;
        }

        try {
            logic.importJsonString(text);
            ShowAlert.showAlertDialogAndWait(getRoot(), AlertType.INFORMATION, "Success", "Successfully imported data!",
                    "Address book data imported successfully!");
            hide();

        } catch (IOException e) {
            logger.warning("Failed to import JSON: " + e.getMessage());
            ShowAlert.showAlertDialogAndWait(getRoot(), AlertType.ERROR, "Import Failed", "Import Failed",
                    "Please ensure that you only paste the content of the entire address book JSON file.");
        }
    }
}
