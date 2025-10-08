package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetStatusCommand object
 */
public class SetStatusCommandParser implements Parser<SetStatusCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetStatusCommand
     * and returns a SetStatusCommand object for execution.
     * Format: status INDEX [STATUS]
     */
    public SetStatusCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetStatusCommand.MESSAGE_USAGE));
        }

        // Split into first token (index) and the rest (optional status)
        int firstSpace = trimmed.indexOf(' ');
        String indexToken = firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace);
        String statusToken = firstSpace == -1 ? "" : trimmed.substring(firstSpace + 1).trim();

        Index index = ParserUtil.parseIndex(indexToken);
        return new SetStatusCommand(index, statusToken);
    }
}
