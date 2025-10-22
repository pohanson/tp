package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class JsonAddressBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");
    private static final String TEMP_FILE = "TempAddressBook.json";
    private static final String TEST_FILE = "test.json";

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new JsonAddressBookStorage(Paths.get(filePath)).readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidPersonAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidAndValidPersonAddressBook.json"));
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve(TEMP_FILE);
        AddressBook original = getTypicalAddressBook();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveAddressBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readAddressBook().get(); // file path not specified
        assertEquals(original, new AddressBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new JsonAddressBookStorage(Paths.get(filePath))
                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
    }

    @Test
    public void readAddressBookFromJsonString_validJson_success() throws Exception {
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

        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        ReadOnlyAddressBook addressBook = storage.readAddressBookFromJsonString(validJson);
        Person currentPerson = addressBook.getPersonList().get(0);
        assertEquals(1, addressBook.getPersonList().size());
        assertEquals("Alice Pauline", currentPerson.getName().toString());
        assertEquals("94351253", currentPerson.getPhone().toString());
        assertEquals("alice@example.com", currentPerson.getEmail().toString());
        assertEquals("123, Jurong West Ave 6, #08-111", currentPerson.getAddress().toString());
        assertTrue(currentPerson.getTags().contains(new Tag("friends")));
    }

    @Test
    public void readAddressBookFromJsonString_invalidJson_throwsDataLoadingException() {
        String invalidJson = "{ not valid json }";
        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        assertThrows(DataLoadingException.class, () -> storage.readAddressBookFromJsonString(invalidJson));
    }

    @Test
    public void readAddressBookFromJsonString_missingPersonFields_throwsDataLoadingException() {
        String jsonWithMissingFields = "{"
                                               + "\"persons\": ["
                                               + "{"
                                               + "\"name\": \"Alice Pauline\""
                                               + "}"
                                               + "]"
                                               + "}";

        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        assertThrows(DataLoadingException.class, () -> storage.readAddressBookFromJsonString(jsonWithMissingFields));
    }

    @Test
    public void readAddressBookFromJsonString_invalidPersonFormat_throwsDataLoadingException() {
        String jsonWithInvalidPerson = "{"
                                               + "\"persons\": ["
                                               + "{"
                                               + "\"name\": \"Alice'Pauline\","
                                               + "\"phone\": \"invalid\","
                                               + "\"email\": \"notanemail\","
                                               + "\"address\": \"123 Street\","
                                               + "\"tags\": []"
                                               + "}"
                                               + "]"
                                               + "}";

        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        assertThrows(DataLoadingException.class, () -> storage.readAddressBookFromJsonString(jsonWithInvalidPerson));
    }

    @Test
    public void readAddressBookFromJsonString_emptyJson_success() throws Exception {
        String emptyJson = "{\"persons\": []}";
        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        ReadOnlyAddressBook addressBook = storage.readAddressBookFromJsonString(emptyJson);

        assertEquals(0, addressBook.getPersonList().size());
    }

    @Test
    public void readAddressBookFromJsonString_multiplePersons_success() throws Exception {
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

        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        ReadOnlyAddressBook addressBook = storage.readAddressBookFromJsonString(multiplePersonsJson);

        assertEquals(2, addressBook.getPersonList().size());
        assertEquals("Alice Pauline", addressBook.getPersonList().get(0).getName().toString());
        assertEquals("Hoon Meier", addressBook.getPersonList().get(1).getName().toString());
    }

    @Test
    public void readAddressBookFromJsonString_emptyString_throwsDataLoadingException() {
        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve(TEST_FILE));
        assertThrows(DataLoadingException.class, () -> storage.readAddressBookFromJsonString(""));
    }

    @Test
    public void parse_validJson_success() throws Exception {
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

        ReadOnlyAddressBook addressBook = JsonAddressBookStorage.parse(validJson);
        Person currentPerson = addressBook.getPersonList().get(0);

        assertEquals(1, addressBook.getPersonList().size());
        assertEquals("Alice Pauline", currentPerson.getName().toString());
        assertEquals("94351253", currentPerson.getPhone().toString());
        assertEquals("alice@example.com", currentPerson.getEmail().toString());
        assertEquals("123, Jurong West Ave 6, #08-111", currentPerson.getAddress().toString());
        assertTrue(currentPerson.getTags().contains(new Tag("friends")));
    }

    @Test
    public void parse_invalidJson_throwsDataLoadingException() {
        String invalidJson = "invalid json";
        assertThrows(DataLoadingException.class, () -> JsonAddressBookStorage.parse(invalidJson));
    }

    @Test
    public void parse_emptyJson_success() throws Exception {
        String emptyJson = "{\"persons\": []}";
        ReadOnlyAddressBook addressBook = JsonAddressBookStorage.parse(emptyJson);

        assertEquals(0, addressBook.getPersonList().size());
    }
}
