package seedu.address.ui;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.StatusViewState;

/**
 * A UI component that displays the current status view state (what statuses are being viewed).
 */
public class StatusViewPanel extends UiPart<Region> {

    private static final String FXML = "StatusViewPanel.fxml";

    @FXML
    private Label statusLabel;

    /**
     * Creates a {@code StatusViewPanel} with the given {@code statusViewStateProperty}.
     * The panel will automatically update when the status view state changes.
     *
     * @param statusViewStateProperty The property containing the current status view state.
     */
    public StatusViewPanel(ReadOnlyObjectProperty<StatusViewState> statusViewStateProperty) {
        super(FXML);

        // Set initial text
        statusLabel.setText(statusViewStateProperty.getValue().getDisplayText());

        // Listen for changes and update the label
        statusViewStateProperty.addListener((observable, oldValue, newValue) -> {
            statusLabel.setText(newValue.getDisplayText());
        });
    }
}

