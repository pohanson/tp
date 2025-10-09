package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

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
     * Creates a {@code SidebarPanel}.
     */
    public SidebarPanel() {
        super(FXML);
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
