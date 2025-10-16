package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;

/**
 * Sets the status of a person in the address book.
 */
public class SetStatusCommand extends Command {
    public static final String COMMAND_WORD = "status";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the contact status of a person.\n"
            + "Parameters: INDEX [STATUS] (default: Uncontacted)\n"
            + "Allowed STATUS: " + Status.allowedValuesDescription() + "\n"
            + "Examples:\nstatus 12 Contacted\nstatus 12 Rejected\nstatus 12 Accepted\n"
            + "status 12 Unreachable\nstatus 12 Busy";

    public static final String MESSAGE_SET_STATUS_SUCCESS = "Status set for Person: %1$s";
    public static final String MESSAGE_INVALID_STATUS = "Invalid status. Allowed: "
            + Status.allowedValuesDescription();

    private final Index targetIndex;
    private final String statusInput;

    /**
     * @param targetIndex of the person in the filtered person list to change status
     * @param statusInput is the status to be set
     */
    public SetStatusCommand(Index targetIndex, String statusInput) {
        this.targetIndex = targetIndex;
        this.statusInput = statusInput;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToSetStatus = lastShownList.get(targetIndex.getZeroBased());
        final Status newStatus;
        if (statusInput == null || statusInput.isBlank()) {
            newStatus = Status.UNCONTACTED;
        } else {
            try {
                newStatus = Status.fromStringIgnoreCase(statusInput);
            } catch (IllegalArgumentException e) {
                throw new CommandException(MESSAGE_INVALID_STATUS);
            }
        }

        Person updatedPerson = new Person(
                personToSetStatus.getName(),
                personToSetStatus.getPhone(),
                personToSetStatus.getEmail(),
                personToSetStatus.getAddress(),
                personToSetStatus.getTags(),
                newStatus
        );

        model.setPerson(personToSetStatus, updatedPerson);
        return new CommandResult(String.format(MESSAGE_SET_STATUS_SUCCESS, Messages.format(updatedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SetStatusCommand)) {
            return false;
        }
        SetStatusCommand o = (SetStatusCommand) other;
        return targetIndex.equals(o.targetIndex)
                && ((statusInput == null && o.statusInput == null)
                || (statusInput != null && statusInput.equals(o.statusInput)));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", targetIndex.getOneBased())
                .add("status", "'" + statusInput + "'")
                .toString();
    }
}
