package seedu.address.model;

import java.util.Objects;

import seedu.address.model.person.Status;

/**
 * Represents the state when viewing/editing a template.
 */
public class TemplateViewState {
    private final Status status;
    private final String content;

    /**
     * Creates a TemplateViewState with the given status and content.
     *
     * @param status The status for which the template is being viewed/edited.
     * @param content The current content of the template.
     */
    public TemplateViewState(Status status, String content) {
        this.status = status;
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TemplateViewState)) {
            return false;
        }

        TemplateViewState otherState = (TemplateViewState) other;
        return status.equals(otherState.status)
                && content.equals(otherState.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, content);
    }

    @Override
    public String toString() {
        return "TemplateViewState{status=" + status + ", contentLength=" + content.length() + "}";
    }
}
