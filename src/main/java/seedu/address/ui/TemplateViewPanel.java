package seedu.address.ui;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.model.TemplateViewState;

/**
 * A UI component that displays the template editor.
 */
public class TemplateViewPanel extends UiPart<Region> {

    private static final String FXML = "TemplateViewPanel.fxml";

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea templateTextArea;

    @FXML
    private Label instructionLabel;

    private final TemplateViewState templateViewState;

    /**
     * Creates a {@code TemplateViewPanel} with the given template view state property.
     */
    public TemplateViewPanel(ReadOnlyObjectProperty<TemplateViewState> templateViewStateProperty) {
        super(FXML);

        this.templateViewState = templateViewStateProperty.getValue();

        if (templateViewState != null) {
            updateView(templateViewState);
        }

        // Listen for changes to template view state
        templateViewStateProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateView(newValue);
            }
        });
    }

    /**
     * Updates the view with the new template state.
     */
    private void updateView(TemplateViewState state) {
        String statusName = formatStatusName(state.getStatus().name());
        statusLabel.setText("Template for " + statusName + " Status");
        templateTextArea.setText(state.getContent());
    }

    /**
     * Returns the current content of the template text area.
     */
    public String getTemplateContent() {
        return templateTextArea.getText();
    }

    /**
     * Formats the status name for display.
     */
    private String formatStatusName(String statusName) {
        return statusName.charAt(0) + statusName.substring(1).toLowerCase();
    }
}
