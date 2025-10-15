package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s name or tags match the given keywords.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;
    private final String statusKeyword;

    /**
     * Constructs a predicate that matches a {@code Person}
     *
     * @param nameKeywords keywords to check against the person's name
     * @param tagKeywords keywords to check against the person's tags
     */
    public PersonMatchesKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords, String statusKeyword) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
        this.statusKeyword = statusKeyword;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesName = nameKeywords.isEmpty() || nameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));

        boolean matchesTag = tagKeywords.isEmpty() || tagKeywords.stream()
                .anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword)));

        boolean matchesStatus = statusKeyword == null || statusKeyword.isEmpty()
                || statusKeyword.equalsIgnoreCase(person.getStatus().name());

        return matchesName && matchesTag && matchesStatus;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonMatchesKeywordsPredicate)) {
            return false;
        }

        PersonMatchesKeywordsPredicate otherPredicate = (PersonMatchesKeywordsPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && tagKeywords.equals(otherPredicate.tagKeywords)
                && (statusKeyword == null ? otherPredicate.statusKeyword == null
                    : statusKeyword.equals(otherPredicate.statusKeyword));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .add("statusKeyword", statusKeyword)
                .toString();
    }

}
