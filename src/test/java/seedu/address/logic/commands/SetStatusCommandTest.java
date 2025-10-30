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
    public void execute_validIndexAndStatus_success() {
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
    public void execute_validIndexBlankStatusDefaultsToUncontacted_success() {
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

    @Test
    public void execute_allValidStatuses_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String[] statuses = {"Contacted", "Uncontacted", "Rejected", "Accepted", "Unreachable", "Busy"};

        for (String statusStr : statuses) {
            Person updated = new PersonBuilder(target).withStatus(statusStr).build();
            SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, statusStr);
            String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                    Messages.format(updated));
            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.setPerson(target, updated);

            assertCommandSuccess(command, model, expectedMessage, expectedModel);
            target = updated; // Update target for next iteration
        }
    }

    @Test
    public void execute_caseInsensitiveStatus_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String[] statusVariants = {"contacted", "CONTACTED", "CoNtAcTeD", "cONTACTED"};

        for (String statusStr : statusVariants) {
            Person updated = new PersonBuilder(target).withStatus("Contacted").build();
            SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, statusStr);
            String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                    Messages.format(updated));
            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.setPerson(target, updated);

            assertCommandSuccess(command, model, expectedMessage, expectedModel);
            target = updated; // Update target for next iteration
        }
    }

    @Test
    public void execute_nullStatusDefaultsToUncontacted_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updated = new PersonBuilder(target).withStatus("Uncontacted").build();
        SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, null);
        String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                Messages.format(updated));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyStringStatusDefaultsToUncontacted_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updated = new PersonBuilder(target).withStatus("Uncontacted").build();
        SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, "");
        String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                Messages.format(updated));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_lastIndexInList_success() {
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Person target = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person updated = new PersonBuilder(target).withStatus("Contacted").build();
        SetStatusCommand command = new SetStatusCommand(lastIndex, "Contacted");
        String expectedMessage = String.format(SetStatusCommand.MESSAGE_SET_STATUS_SUCCESS,
                Messages.format(updated));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals_nullStatus_correctComparison() {
        SetStatusCommand commandWithNull1 = new SetStatusCommand(INDEX_FIRST_PERSON, null);
        SetStatusCommand commandWithNull2 = new SetStatusCommand(INDEX_FIRST_PERSON, null);
        SetStatusCommand commandWithStatus = new SetStatusCommand(INDEX_FIRST_PERSON, "Contacted");

        assertEquals(commandWithNull1, commandWithNull2);
        assertNotEquals(commandWithNull1, commandWithStatus);
    }

    @Test
    public void toString_nullStatus_correctFormat() {
        SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, null);
        String expected = SetStatusCommand.class.getCanonicalName() + "{index=1, status='null'}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void execute_multipleInvalidStatusVariations_failure() {
        String[] invalidStatuses = {"invalid", "WRONG", "123", "Con tacted", "contacted123",
                                    "contact", "reject", "accept"};

        for (String invalidStatus : invalidStatuses) {
            SetStatusCommand command = new SetStatusCommand(INDEX_FIRST_PERSON, invalidStatus);
            assertCommandFailure(command, model, SetStatusCommand.MESSAGE_INVALID_STATUS);
        }
    }
}

