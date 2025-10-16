package seedu.address.logic.clipboard;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * System clipboard implementation for copying and pasting
 */
public class SystemClipboardProvider implements ClipboardProvider {
    @Override
    public String getString() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return clipboard.hasString() ? clipboard.getString() : null;
    }

    @Override
    public void setString(String value) {
        ClipboardContent content = new ClipboardContent();
        // Need to ensure no NullPointerException
        content.putString(value == null ? "" : value);
        Clipboard.getSystemClipboard().setContent(content);
    }
}


