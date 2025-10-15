package seedu.address.ui;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.StatusViewState;

/**
 * Panel containing the sidebar with status and tags sections.
 */
public class SidebarPanel extends UiPart<Region> {
    private static final String FXML = "SidebarPanel.fxml";

    @FXML
    private StackPane statusSectionPlaceholder;

    @FXML
    private StackPane tagsSectionPlaceholder;

    /**
     * Creates a {@code SidebarPanel} with the given status view state property.
     *
     * @param statusViewStateProperty The property containing the current status view state.
     */
    public SidebarPanel(ReadOnlyObjectProperty<StatusViewState> statusViewStateProperty) {
        super(FXML);

        // Create and add the StatusViewPanel
        StatusViewPanel statusViewPanel = new StatusViewPanel(statusViewStateProperty);
        statusSectionPlaceholder.getChildren().clear();
        statusSectionPlaceholder.getChildren().add(statusViewPanel.getRoot());
    }

    /**
     * Returns the status section placeholder for future implementation.
     */
    public StackPane getStatusSectionPlaceholder() {
        return statusSectionPlaceholder;
    }

    /**
     * Returns the tags section placeholder for future implementation.
     */
    public StackPane getTagsSectionPlaceholder() {
        return tagsSectionPlaceholder;
    }
}
