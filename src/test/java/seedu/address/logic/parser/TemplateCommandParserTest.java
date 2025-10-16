package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TemplateCommand;
import seedu.address.model.person.Status;
import seedu.address.storage.TemplateStorageManagerTest.StorageStub;

public class TemplateCommandParserTest {

    private final StorageStub storageStub = new StorageStub();
    private TemplateCommandParser parser = new TemplateCommandParser(storageStub);

    @Test
    public void parse_validContactedStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:CONTACTED",
                new TemplateCommand(Status.CONTACTED, storageStub));
    }

    @Test
    public void parse_validUncontactedStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:UNCONTACTED",
                new TemplateCommand(Status.UNCONTACTED, storageStub));
    }

    @Test
    public void parse_validRejectedStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:REJECTED",
                new TemplateCommand(Status.REJECTED, storageStub));
    }

    @Test
    public void parse_validAcceptedStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:ACCEPTED",
                new TemplateCommand(Status.ACCEPTED, storageStub));
    }

    @Test
    public void parse_validUnreachableStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:UNREACHABLE",
                new TemplateCommand(Status.UNREACHABLE, storageStub));
    }

    @Test
    public void parse_validBusyStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:BUSY",
                new TemplateCommand(Status.BUSY, storageStub));
    }

    @Test
    public void parse_validSaveCommand_returnsTemplateCommand() {
        assertParseSuccess(parser, " save",
                new TemplateCommand(storageStub));
    }

    @Test
    public void parse_lowercaseStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:contacted",
                new TemplateCommand(Status.CONTACTED, storageStub));
    }

    @Test
    public void parse_mixedCaseStatus_returnsTemplateCommand() {
        assertParseSuccess(parser, " s:CoNtAcTeD",
                new TemplateCommand(Status.CONTACTED, storageStub));
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        assertParseFailure(parser, " s:INVALID",
                TemplateCommand.MESSAGE_INVALID_STATUS);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingStatusPrefix_throwsParseException() {
        assertParseFailure(parser, " CONTACTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingStatusValue_throwsParseException() {
        assertParseFailure(parser, " s:",
                TemplateCommand.MESSAGE_INVALID_STATUS);
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertParseFailure(parser, " x:CONTACTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        assertParseFailure(parser, " s:CONTACTED extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_saveWithExtraArguments_throwsParseException() {
        assertParseFailure(parser, " save extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleStatuses_throwsParseException() {
        assertParseFailure(parser, " s:CONTACTED s:REJECTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TemplateCommand.MESSAGE_USAGE));
    }
}
