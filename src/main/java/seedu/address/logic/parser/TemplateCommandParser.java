package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.clipboard.ClipboardProvider;
import seedu.address.logic.clipboard.SystemClipboardProvider;
import seedu.address.logic.commands.TemplateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Status;
import seedu.address.storage.TemplateStorage;

/**
 * Parses input arguments and creates a new TemplateCommand object.
 */
public class TemplateCommandParser implements Parser<TemplateCommand> {

    private static final String SAVE_KEYWORD = "save";
    private static final String COPY_KEYWORD = "copy";
    private static final Prefix PREFIX_STATUS = new Prefix("s:");

    private final TemplateStorage templateStorage;
    private final ClipboardProvider clipboardProvider;

    /**
     * Creates a TemplateCommandParser with the given template storage.
     *
     * @param templateStorage The storage for reading and writing templates.
     */
    public TemplateCommandParser(TemplateStorage templateStorage) {
        this(templateStorage, new SystemClipboardProvider());
    }

    /**
     * Creates a TemplateCommandParser with the given template storage and clipboard provider.
     * This constructor is primarily for testing.
     *
     * @param templateStorage The storage for reading and writing templates.
     * @param clipboardProvider The provider for accessing the clipboard.
     */
    public TemplateCommandParser(TemplateStorage templateStorage, ClipboardProvider clipboardProvider) {
        this.templateStorage = templateStorage;
        this.clipboardProvider = clipboardProvider;
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
            return new TemplateCommand(templateStorage);
        }

        // Check for extra arguments after save keyword
        if (trimmedArgs.toLowerCase().startsWith(SAVE_KEYWORD + " ")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        // Check if this is a copy command
        if (trimmedArgs.toLowerCase().startsWith(COPY_KEYWORD + " ")) {
            return parseCopyCommand(trimmedArgs.substring((COPY_KEYWORD + " ").length()));
        }

        // Check for copy keyword without space
        if (trimmedArgs.equalsIgnoreCase(COPY_KEYWORD)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
        }

        // Otherwise, parse as status command (open template)
        return parseOpenCommand(args);
    }

    /**
     * Parses arguments for a copy command.
     *
     * @param args The arguments following "copy ".
     * @return A TemplateCommand for copying a template.
     * @throws ParseException If the arguments are invalid.
     */
    private TemplateCommand parseCopyCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_STATUS);

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
            return new TemplateCommand(status, templateStorage, clipboardProvider);
        } catch (IllegalArgumentException e) {
            throw new ParseException(TemplateCommand.MESSAGE_INVALID_STATUS);
        }
    }

    /**
     * Parses arguments for an open command.
     *
     * @param args The arguments for opening a template.
     * @return A TemplateCommand for opening a template.
     * @throws ParseException If the arguments are invalid.
     */
    private TemplateCommand parseOpenCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_STATUS);

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
            return new TemplateCommand(status, templateStorage);
        } catch (IllegalArgumentException e) {
            throw new ParseException(TemplateCommand.MESSAGE_INVALID_STATUS);
        }
    }
}
