package seedu.address.model.person;

import java.util.Locale;

/**
 * Represents the contact status of a person.
 */
public class Status {
    public static final Status ACCEPTED = new Status(StatusValue.ACCEPTED);
    public static final Status BUSY = new Status(StatusValue.BUSY);
    public static final Status CONTACTED = new Status(StatusValue.CONTACTED);
    public static final Status REJECTED = new Status(StatusValue.REJECTED);
    public static final Status UNCONTACTED = new Status(StatusValue.UNCONTACTED);
    public static final Status UNREACHABLE = new Status(StatusValue.UNREACHABLE);

    public static final String MESSAGE_CONSTRAINTS = "Status should be one of the following: "
            + allowedValuesDescription();
    public static final String VALIDATION_REGEX = "^(Uncontacted|Contacted|Rejected|Accepted|Unreachable|Busy)$";

    /**
     * Enum containing the constant status values.
     */
    public enum StatusValue {
        UNCONTACTED,
        CONTACTED,
        REJECTED,
        ACCEPTED,
        UNREACHABLE,
        BUSY
    }

    private final StatusValue value;

    private Status(StatusValue value) {
        this.value = value;
    }

    public StatusValue getValue() {
        return value;
    }

    public String name() {
        return value.name();
    }

    /**
     * Returns true if the provided string corresponds to a valid {@code Status}, ignoring case.
     *
     * @param test the status string to validate
     * @return true if {@code test} maps to a known status value
     */
    public static boolean isValidStatus(String test) {
        try {
            fromStringIgnoreCase(test);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Status)) {
            return false;
        }
        Status otherStatus = (Status) other;
        return value.equals(otherStatus.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.name();
    }

    /**
     * Returns a {@code Status} instance corresponding to the given string value.
     * If the input is null, return UNCONTACTED by default.
     *
     * @param value the string representation of the status
     * @return the corresponding {@code Status} instance
     * @throws IllegalArgumentException if the input does not match any valid status
     */
    public static Status fromStringIgnoreCase(String value) {
        if (value == null) {
            return UNCONTACTED;
        }
        String normalised = value.trim().toUpperCase(Locale.ROOT);
        switch (normalised) {
        case "UNCONTACTED":
            return UNCONTACTED;
        case "CONTACTED":
            return CONTACTED;
        case "REJECTED":
            return REJECTED;
        case "ACCEPTED":
            return ACCEPTED;
        case "UNREACHABLE":
            return UNREACHABLE;
        case "BUSY":
            return BUSY;
        default:
            throw new IllegalArgumentException("Invalid status: " + value);
        }
    }

    public static String allowedValuesDescription() {
        return "Uncontacted, Contacted, Rejected, Accepted, Unreachable, Busy";
    }
}
