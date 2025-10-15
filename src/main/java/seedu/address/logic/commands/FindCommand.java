package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.StatusViewState;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.model.person.Status;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose name or tag matches any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: n:[name] t:[tag] ..\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "alice\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "free\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STATUS + "uncontacted\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "[" + PREFIX_STATUS + "STATUS]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "alice\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "free\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STATUS + "uncontacted";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        // Update status view state based on whether status filter is applied
        updateStatusViewState(model);

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    /**
     * Updates the status view state in the model based on the predicate used for filtering.
     *
     * @param model The model to update the status view state in.
     */
    private void updateStatusViewState(Model model) {
        if (predicate instanceof PersonMatchesKeywordsPredicate) {
            PersonMatchesKeywordsPredicate pred = (PersonMatchesKeywordsPredicate) predicate;
            String statusKeyword = pred.getStatusKeyword();

            if (statusKeyword != null && !statusKeyword.isEmpty()) {
                try {
                    Status status = Status.fromStringIgnoreCase(statusKeyword);
                    model.setStatusViewState(new StatusViewState(status));
                } catch (IllegalArgumentException e) {
                    // If status parsing fails, default to showing all statuses
                    model.setStatusViewState(StatusViewState.ALL_STATUSES);
                }
            } else {
                model.setStatusViewState(StatusViewState.ALL_STATUSES);
            }
        } else {
            // For other predicates (e.g., NameContainsKeywordsPredicate), show all statuses
            model.setStatusViewState(StatusViewState.ALL_STATUSES);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
