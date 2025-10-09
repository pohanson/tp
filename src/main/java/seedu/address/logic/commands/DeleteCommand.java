package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes one or more persons identified using their displayed indices from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
                                                       + ": Deletes the person(s) identified by the index number(s) "
                                                       + "used in the displayed person list.\n"
                                                       + "Parameters: INDEX [MORE_INDICES]... (must be positive "
                                                       + "integers)\n"
                                                       + "Example: "
                                                       + COMMAND_WORD
                                                       + " 1\n"
                                                       + "Example: "
                                                       + COMMAND_WORD
                                                       + " 1 2 3";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted %1$d Person(s):\n%2$s";

    private final List<Index> targetIndices;

    public DeleteCommand(List<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices first
        for (Index index : targetIndices) {
            if (index.getZeroBased()
                        >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Sort indices in descending order to avoid index shifting issues when deleting
        List<Index> sortedIndices = new ArrayList<>(targetIndices);
        sortedIndices.sort(Comparator.comparingInt(Index::getZeroBased).reversed());

        // Collect persons to delete
        List<Person> personsToDelete = new ArrayList<>();
        for (Index index : sortedIndices) {
            personsToDelete.add(lastShownList.get(index.getZeroBased()));
        }

        // Delete all persons
        for (Person person : personsToDelete) {
            model.deletePerson(person);
        }

        // Format success message
        if (personsToDelete.size()
                    == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                    Messages.format(personsToDelete.get(0))));
        } else {
            StringBuilder deletedPersons = new StringBuilder();
            for (Person person : personsToDelete) {
                deletedPersons.append("- ").append(Messages.format(person)).append("\n");
            }
            return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS,
                    personsToDelete.size(), deletedPersons.toString().trim()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other
                    == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }

        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                       .add("targetIndices", targetIndices)
                       .toString();
    }
}
