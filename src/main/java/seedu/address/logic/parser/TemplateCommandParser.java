package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.TemplateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Status;
import seedu.address.storage.Storage;

/**
 * Parses input arguments and creates a new TemplateCommand object.
 */
public class TemplateCommandParser implements Parser<TemplateCommand> {

    private static final String SAVE_KEYWORD = "save";
    private static final Prefix PREFIX_STATUS = new Prefix("s:");

    private final Storage storage;

    /**
     * Creates a TemplateCommandParser with the given storage.
     */
    public TemplateCommandParser(Storage storage) {
        this.storage = storage;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the TemplateCommand
     * and returns a TemplateCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public TemplateCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        // Check if this is a save command
        if (trimmedArgs.equalsIgnoreCase(SAVE_KEYWORD)) {
            return new TemplateCommand(storage);
        }

        // Check for extra arguments after save keyword
        if (trimmedArgs.toLowerCase().startsWith(SAVE_KEYWORD + " ")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        // Otherwise, parse as status command
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STATUS);

        // Check for preamble (extra text before s:)
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        // Check for multiple status values
        if (argMultimap.getAllValues(PREFIX_STATUS).size() > 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        String statusString = argMultimap.getValue(PREFIX_STATUS).get();

        // Validate that status string doesn't contain extra arguments (spaces)
        if (statusString.contains(" ")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        try {
            Status status = Status.fromStringIgnoreCase(statusString);
            return new TemplateCommand(status, storage);
        } catch (IllegalArgumentException e) {
            throw new ParseException(TemplateCommand.MESSAGE_INVALID_STATUS);
        }
    }
}
