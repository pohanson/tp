package seedu.address.ui;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.TagsViewState;

/**
 * A UI component that displays the current tags view state (what tags are being viewed).
 */
public class TagsViewPanel extends UiPart<Region> {

    private static final String FXML = "TagsViewPanel.fxml";

    @FXML
    private Label tagsLabel;

    /**
     * Creates a {@code TagsViewPanel} with the given {@code tagsViewStateProperty}.
     * The panel will automatically update when the tags view state changes.
     *
     * @param tagsViewStateProperty The property containing the current tags view state.
     */
    public TagsViewPanel(ReadOnlyObjectProperty<TagsViewState> tagsViewStateProperty) {
        super(FXML);

        // Set initial text
        tagsLabel.setText(tagsViewStateProperty.getValue().getDisplayText());

        // Listen for changes and update the label
        tagsViewStateProperty.addListener((observable, oldValue, newValue) -> {
            tagsLabel.setText(newValue.getDisplayText());
        });
    }
}

