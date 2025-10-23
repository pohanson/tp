package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TemplateViewState;
import seedu.address.model.person.Status;
import seedu.address.storage.TemplateStorage;

/**
 * Opens a template view for editing email templates for a specific status.
 */
public class TemplateCommand extends Command {

    public static final String COMMAND_WORD = "template";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Opens template editor for a specific status or saves the current template.\n"
            + "Parameters: s:STATUS (to open template) or save (to save current template)\n"
            + "Allowed STATUS: " + Status.allowedValuesDescription() + "\n"
            + "Examples:\n"
            + COMMAND_WORD + " s:Contacted\n"
            + COMMAND_WORD + " s:Uncontacted\n"
            + COMMAND_WORD + " save";

    public static final String MESSAGE_OPEN_TEMPLATE_SUCCESS = "Opened template for %s status";
    public static final String MESSAGE_SAVE_TEMPLATE_SUCCESS = "Template saved for %s status";
    public static final String MESSAGE_NO_TEMPLATE_TO_SAVE = "No template is currently open. "
            + "Use 'template s:STATUS' to open a template first.";
    public static final String MESSAGE_INVALID_STATUS = "Invalid status. Allowed: "
            + Status.allowedValuesDescription();
    public static final String MESSAGE_STORAGE_ERROR = "Error accessing template storage: %s";

    private final Status status;
    private final boolean isSaveAction;
    private final TemplateStorage templateStorage;

    /**
     * Creates a TemplateCommand to open a template for the specified status.
     *
     * @param status The status for which to open the template.
     * @param templateStorage The storage to read/write templates.
     */
    public TemplateCommand(Status status, TemplateStorage templateStorage) {
        requireNonNull(status);
        requireNonNull(templateStorage);
        this.status = status;
        this.isSaveAction = false;
        this.templateStorage = templateStorage;
    }

    /**
     * Creates a TemplateCommand to save the currently open template.
     *
     * @param templateStorage The storage to read/write templates.
     */
    public TemplateCommand(TemplateStorage templateStorage) {
        requireNonNull(templateStorage);
        this.status = null;
        this.isSaveAction = true;
        this.templateStorage = templateStorage;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (isSaveAction) {
            return executeSave(model);
        } else {
            return executeOpen(model);
        }
    }

    /**
     * Opens the template view for the specified status.
     *
     * @param model The model containing the application data.
     * @return A CommandResult indicating success with the template view flag set.
     * @throws CommandException If there's an error accessing template storage.
     */
    private CommandResult executeOpen(Model model) throws CommandException {
        try {
            String content = templateStorage.readTemplate(status);
            TemplateViewState state = new TemplateViewState(status, content);
            model.setTemplateViewState(state);

            String successMessage = createOpenSuccessMessage();
            return new CommandResult(successMessage, false, false, true); // isShowTemplate = true
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_STORAGE_ERROR, e.getMessage()));
        }
    }

    /**
     * Saves the currently displayed template.
     *
     * @param model The model containing the current template state.
     * @return A CommandResult indicating success.
     * @throws CommandException If no template is open or there's an error saving.
     */
    private CommandResult executeSave(Model model) throws CommandException {
        TemplateViewState currentState = model.getTemplateViewStateProperty().getValue();

        if (currentState == null) {
            throw new CommandException(MESSAGE_NO_TEMPLATE_TO_SAVE);
        }

        try {
            templateStorage.saveTemplate(currentState.getStatus(), currentState.getContent());
            String successMessage = createSaveSuccessMessage(currentState.getStatus());
            return new CommandResult(successMessage);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_STORAGE_ERROR, e.getMessage()));
        }
    }

    /**
     * Creates a success message for opening a template.
     *
     * @return The formatted success message.
     */
    private String createOpenSuccessMessage() {
        return String.format(MESSAGE_OPEN_TEMPLATE_SUCCESS, formatStatusName(status));
    }

    /**
     * Creates a success message for saving a template.
     *
     * @param statusToSave The status of the template being saved.
     * @return The formatted success message.
     */
    private String createSaveSuccessMessage(Status statusToSave) {
        return String.format(MESSAGE_SAVE_TEMPLATE_SUCCESS, formatStatusName(statusToSave));
    }

    /**
     * Formats the status name for display.
     *
     * @param statusToFormat The status to format.
     * @return A formatted status name (e.g., "Contacted" instead of "CONTACTED").
     */
    private String formatStatusName(Status statusToFormat) {
        String name = statusToFormat.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    /**
     * Checks if this TemplateCommand is equal to another object.
     * Two TemplateCommands are equal if they are both save actions, or if they
     * open templates for the same status.
     *
     * @param other The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TemplateCommand)) {
            return false;
        }

        TemplateCommand otherCommand = (TemplateCommand) other;
        if (isSaveAction != otherCommand.isSaveAction) {
            return false;
        }

        if (isSaveAction) {
            return true; // Both are actions to save
        }

        return status.equals(otherCommand.status);
    }

    /**
     * Returns a string representation of this TemplateCommand.
     *
     * @return A string describing this command.
     */
    @Override
    public String toString() {
        if (isSaveAction) {
            return "TemplateCommand{save}";
        } else {
            return "TemplateCommand{status=" + status + "}";
        }
    }
}
