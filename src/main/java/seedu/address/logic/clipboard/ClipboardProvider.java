package seedu.address.logic.clipboard;

/**
 * Abstraction for system clipboard, needed to enable testing.
 */
public interface ClipboardProvider {
    String getString();
    void setString(String value);
}


