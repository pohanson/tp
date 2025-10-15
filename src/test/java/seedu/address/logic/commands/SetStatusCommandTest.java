package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Integration tests (interaction with {@link Model}) for {@link SetStatusCommand}.
 */
public class SetStatusCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndex_validStatus_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updated = new PersonBuilder(target).withStatus("Contacted").build();
        SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, "Contacted");
        String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                Messages.format(updated));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndex_blankStatus_defaultsToUncontacted_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updated = new PersonBuilder(target).withStatus("Uncontacted").build();
        SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, "      ");
        String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                Messages.format(updated));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SetStatusCommand command = new SetStatusCommand(outOfBoundIndex, "Contacted");
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidStatus_failure() {
        SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, "wawaweewa");
        assertCommandFailure(command, model, SetStatusCommand.MESSAGE_INVALID_STATUS);
    }

    @Test
    public void equals_and_toString() {
        SetStatusCommand a = new SetStatusCommand(Index.fromOneBased(1), "Contacted");
        SetStatusCommand aCopy = new SetStatusCommand(Index.fromOneBased(1), "Contacted");
        SetStatusCommand b = new SetStatusCommand(Index.fromOneBased(2), "Contacted");
        SetStatusCommand c = new SetStatusCommand(Index.fromOneBased(1), "Rejected");

        assertEquals(a, a);
        assertEquals(a, aCopy);
        assertNotEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(null, a);
        assertNotEquals(5, a);

        String expected = SetStatusCommand.class.getCanonicalName() + "{index=1, status='Contacted'}";
        assertEquals(expected, a.toString());
    }
}

