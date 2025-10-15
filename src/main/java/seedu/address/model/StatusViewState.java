package seedu.address.model;

import java.util.Objects;

import seedu.address.model.person.Status;

/**
 * Represents the current status filter view state in the address book.
 * This tracks what status is currently being viewed/filtered in the UI.
 */
public class StatusViewState {

    /** Constant representing that all statuses are being viewed (no status filter applied) */
    public static final StatusViewState ALL_STATUSES = new StatusViewState(null);

    private final Status filterStatus; // null means viewing all statuses

    /**
     * Creates a StatusViewState with the given filter status.
     *
     * @param filterStatus The status being filtered, or null if viewing all statuses.
     */
    public StatusViewState(Status filterStatus) {
        this.filterStatus = filterStatus;
    }

    /**
     * Returns true if currently showing all statuses (no filter applied).
     */
    public boolean isShowingAll() {
        return filterStatus == null;
    }

    /**
     * Returns the filter status, or null if showing all.
     */
    public Status getFilterStatus() {
        return filterStatus;
    }

    /**
     * Returns the display text for this view state.
     */
    public String getDisplayText() {
        if (isShowingAll()) {
            return "Viewing: All Statuses";
        }
        return "Viewing: " + formatStatusName(filterStatus) + " Only";
    }

    /**
     * Formats the status name for display by making it lowercase
     */
    private String formatStatusName(Status status) {
        String name = status.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StatusViewState)) {
            return false;
        }

        StatusViewState otherState = (StatusViewState) other;
        return Objects.equals(filterStatus, otherState.filterStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterStatus);
    }

    @Override
    public String toString() {
        if (isShowingAll()) {
            return "StatusViewState{ALL_STATUSES}";
        }
        return "StatusViewState{filterStatus=" + filterStatus + "}";
    }
}

