package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.TemplateStorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");
    private static final String ADDRESS_BOOK_FILE = "addressBook.json";
    private static final String USER_PREFS_FILE = "userPrefs.json";
    private static final String EXCEPTION_USER_PREFS_FILE = "ExceptionUserPrefs.json";

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve(ADDRESS_BOOK_FILE));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve(USER_PREFS_FILE));
        TemplateStorageManager templateStorage = new TemplateStorageManager(temporaryFolder.resolve("templates"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, templateStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        String expectedMessage = String.format(
                seedu.address.logic.commands.DeleteCommand.MESSAGE_INVALID_INDICES, "9");
        assertCommandException(deleteCommand, expectedMessage);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve(EXCEPTION_USER_PREFS_FILE);

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve(EXCEPTION_USER_PREFS_FILE));
        TemplateStorageManager templateStorage = new TemplateStorageManager(temporaryFolder.resolve("templates"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, templateStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void importJsonString_validJson_success() throws Exception {
        String validJson = "{"
                                   + "\"persons\": ["
                                   + "{"
                                   + "\"name\": \"Alice Pauline\","
                                   + "\"phone\": \"94351253\","
                                   + "\"email\": \"alice@example.com\","
                                   + "\"address\": \"123, Jurong West Ave 6, #08-111\","
                                   + "\"tags\": [\"friends\"]"
                                   + "}"
                                   + "]"
                                   + "}";

        logic.importJsonString(validJson);
        Person currentPerson = model.getFilteredPersonList().get(0);
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals("Alice Pauline", currentPerson.getName().toString());
        assertEquals("94351253", currentPerson.getPhone().toString());
        assertEquals("alice@example.com", currentPerson.getEmail().toString());
        assertEquals("123, Jurong West Ave 6, #08-111", currentPerson.getAddress().toString());
        assertTrue(currentPerson.getTags().contains(new Tag("friends")));
    }

    @Test
    public void importJsonString_invalidJson_throwsException() {
        String invalidJson = "{ not valid json }";
        assertThrows(Exception.class, () -> logic.importJsonString(invalidJson));
    }

    @Test
    public void importJsonString_replacesExistingData_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                                    + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);
        assertEquals(1, model.getFilteredPersonList().size());

        String validJson = "{"
                                   + "\"persons\": ["
                                   + "{"
                                   + "\"name\": \"Alice Pauline\","
                                   + "\"phone\": \"94351253\","
                                   + "\"email\": \"alice@example.com\","
                                   + "\"address\": \"123, Jurong West Ave 6, #08-111\","
                                   + "\"tags\": [\"friends\"]"
                                   + "}"
                                   + "]"
                                   + "}";

        logic.importJsonString(validJson);
        Person currentPerson = model.getFilteredPersonList().get(0);
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals("Alice Pauline", currentPerson.getName().toString());
        assertEquals("94351253", currentPerson.getPhone().toString());
        assertEquals("alice@example.com", currentPerson.getEmail().toString());
        assertEquals("123, Jurong West Ave 6, #08-111", currentPerson.getAddress().toString());

        assertTrue(currentPerson.getTags().contains(new Tag("friends")));
    }

    @Test
    public void importJsonString_emptyJson_clearsAddressBook() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                                    + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);
        assertEquals(1, model.getFilteredPersonList().size());

        String emptyJson = "{\"persons\": []}";
        logic.importJsonString(emptyJson);
        assertEquals(0, model.getFilteredPersonList().size());
    }

    @Test
    public void importJsonString_multiplePersons_success() throws Exception {
        String multiplePersonsJson = "{"
                                             + "\"persons\": ["
                                             + "{"
                                             + "\"name\": \"Alice Pauline\","
                                             + "\"phone\": \"94351253\","
                                             + "\"email\": \"alice@example.com\","
                                             + "\"address\": \"123, Jurong West Ave 6, #08-111\","
                                             + "\"tags\": [\"friends\"]"
                                             + "},"
                                             + "{"
                                             + "\"name\": \"Hoon Meier\","
                                             + "\"phone\": \"8482424\","
                                             + "\"email\": \"stefan@example.com\","
                                             + "\"address\": \"little india\","
                                             + "\"tags\": []"
                                             + "}"
                                             + "]"
                                             + "}";

        logic.importJsonString(multiplePersonsJson);
        assertEquals(2, model.getFilteredPersonList().size());
        assertEquals("Alice Pauline", model.getFilteredPersonList().get(0).getName().toString());
        assertEquals("Hoon Meier", model.getFilteredPersonList().get(1).getName().toString());
    }

    @Test
    public void getTemplateViewState_initiallyNull() {
        assertEquals(null, logic.getTemplateViewState());
    }

    @Test
    public void setTemplateViewState_validState_setsState() {
        seedu.address.model.TemplateViewState state =
                new seedu.address.model.TemplateViewState(
                        seedu.address.model.person.Status.CONTACTED, "Test content");
        logic.setTemplateViewState(state);
        assertEquals(state, logic.getTemplateViewState());
    }

    @Test
    public void setTemplateViewState_nullState_setsNull() {
        // First set a state
        seedu.address.model.TemplateViewState state =
                new seedu.address.model.TemplateViewState(
                        seedu.address.model.person.Status.CONTACTED, "Test content");
        logic.setTemplateViewState(state);
        assertEquals(state, logic.getTemplateViewState());

        // Then set to null
        logic.setTemplateViewState(null);
        assertEquals(null, logic.getTemplateViewState());
    }

    @Test
    public void getTemplateViewState_changesWithModel() {
        seedu.address.model.TemplateViewState state =
                new seedu.address.model.TemplateViewState(
                        seedu.address.model.person.Status.REJECTED, "Rejection template");
        model.setTemplateViewState(state);

        // Logic should reflect model's state
        assertEquals(state, logic.getTemplateViewState());
    }

    @Test
    public void setTemplateViewState_updatesModel() {
        seedu.address.model.TemplateViewState state =
                new seedu.address.model.TemplateViewState(
                        seedu.address.model.person.Status.ACCEPTED, "Acceptance template");
        logic.setTemplateViewState(state);

        // Model should be updated
        assertEquals(state, model.getTemplateViewState());
    }
}
