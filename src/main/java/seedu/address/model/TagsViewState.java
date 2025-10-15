package seedu.address.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the current tags filter view state in the address book.
 * This tracks what tags are currently being viewed/filtered in the UI.
 */
public class TagsViewState {

    /** Constant representing that all tags are being viewed (no tag filter applied) */
    public static final TagsViewState ALL_TAGS = new TagsViewState(null);

    private final List<String> filteredTags; // null or empty means viewing all tags

    /**
     * Creates a TagsViewState with the given filtered tags.
     *
     * @param filteredTags The list of tags being filtered, or null if viewing all tags.
     */
    public TagsViewState(List<String> filteredTags) {
        this.filteredTags = filteredTags == null ? null : new ArrayList<>(filteredTags);
    }

    /**
     * Returns true if currently showing all tags (no filter applied).
     */
    public boolean isShowingAll() {
        return filteredTags == null || filteredTags.isEmpty();
    }

    /**
     * Returns an immutable list of the filtered tags.
     */
    public List<String> getFilteredTags() {
        if (filteredTags == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(filteredTags);
    }

    /**
     * Returns the display text for this view state.
     */
    public String getDisplayText() {
        if (isShowingAll()) {
            return "Viewing: All Tags";
        }
        return "Viewing: " + String.join(", ", filteredTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagsViewState)) {
            return false;
        }

        TagsViewState otherState = (TagsViewState) other;
        return Objects.equals(filteredTags, otherState.filteredTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filteredTags);
    }

    @Override
    public String toString() {
        if (isShowingAll()) {
            return "TagsViewState{ALL_TAGS}";
        }
        return "TagsViewState{filteredTags=" + filteredTags + "}";
    }
}

