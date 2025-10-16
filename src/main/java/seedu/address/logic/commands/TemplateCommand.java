package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.StatusViewState;
import seedu.address.model.TemplateViewState;
import seedu.address.model.person.Status;
import seedu.address.storage.Storage;

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
    private final Storage storage;

    /**
     * Creates a TemplateCommand to open a template for the specified status.
     *
     * @param status The status for which to open the template.
     * @param storage The storage to read/write templates.
     */
    public TemplateCommand(Status status, Storage storage) {
        requireNonNull(status);
        requireNonNull(storage);
        this.status = status;
        this.isSaveAction = false;
        this.storage = storage;
    }

    /**
     * Creates a TemplateCommand to save the currently open template.
     *
     * @param storage The storage to read/write templates.
     */
    public TemplateCommand(Storage storage) {
        requireNonNull(storage);
        this.status = null;
        this.isSaveAction = true;
        this.storage = storage;
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
     */
    private CommandResult executeOpen(Model model) throws CommandException {
        try {
            String content = storage.readTemplate(status);
            TemplateViewState state = new TemplateViewState(status, content);
            model.setTemplateViewState(state);
            
            model.setStatusViewState(new StatusViewState(status));

            return new CommandResult(
                    String.format(MESSAGE_OPEN_TEMPLATE_SUCCESS, formatStatusName(status)),
                    false, false, true); // isShowTemplate = true
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_STORAGE_ERROR, e.getMessage()));
        }
    }

    /**
     * Saves the currently displayed template.
     */
    private CommandResult executeSave(Model model) throws CommandException {
        TemplateViewState currentState = model.getTemplateViewStateProperty().getValue();

        if (currentState == null) {
            throw new CommandException(MESSAGE_NO_TEMPLATE_TO_SAVE);
        }

        try {
            storage.saveTemplate(currentState.getStatus(), currentState.getContent());
            model.setStatusViewState(StatusViewState.ALL_STATUSES);
            return new CommandResult(
                    String.format(MESSAGE_SAVE_TEMPLATE_SUCCESS, formatStatusName(currentState.getStatus())));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_STORAGE_ERROR, e.getMessage()));
        }
    }

    /**
     * Formats the status name for display.
     */
    private String formatStatusName(Status status) {
        String name = status.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

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

    @Override
    public String toString() {
        if (isSaveAction) {
            return "TemplateCommand{save}";
        } else {
            return "TemplateCommand{status=" + status + "}";
        }
    }
}
