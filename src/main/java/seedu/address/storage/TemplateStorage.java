package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.model.person.Status;

/**
 * Represents a storage for email templates.
 */
public interface TemplateStorage {

    /**
     * Returns the base directory path where templates are stored.
     */
    Path getTemplateDirectoryPath();

    /**
     * Returns the template content for the given status.
     * If the file does not exist, returns the default template.
     *
     * @param status The status for which to retrieve the template.
     * @return The template content.
     * @throws IOException if there was any problem reading the file.
     */
    String readTemplate(Status status) throws IOException;

    /**
     * Saves the given template content for the specified status.
     *
     * @param status The status for which to save the template.
     * @param content The template content to save.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTemplate(Status status, String content) throws IOException;

    /**
     * Returns the default template text for a given status.
     *
     * @param status The status for which to get the default template.
     * @return The default template text.
     */
    String getDefaultTemplate(Status status);
}
