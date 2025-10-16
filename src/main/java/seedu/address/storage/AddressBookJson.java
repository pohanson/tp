package seedu.address.storage;

import java.io.IOException;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Parse JSON.
 */
public final class AddressBookJson {
    private AddressBookJson() {}

    /**
     * Parses a JSON string into a ReadOnlyAddressBook.
     * @throws DataLoadingException if parsing fails or values are illegal
     */
    public static ReadOnlyAddressBook parse(String jsonString) throws DataLoadingException {
        try {
            JsonSerializableAddressBook json = JsonUtil.fromJsonString(jsonString, JsonSerializableAddressBook.class);
            return json.toModelType();
        } catch (IOException | IllegalValueException e) {
            throw new DataLoadingException(e);
        }
    }
}


