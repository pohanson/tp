package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Status;
import seedu.address.storage.TemplateStorageManagerTest.StorageStub;

import java.nio.file.Path;

public class TemplateCommandTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void constructor_nullStatus_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TemplateCommand(null, false));
    }

    @Test
    public void constructor_saveMode_doesNotThrowException() {
        // This should not throw an exception even though status is null
        new TemplateCommand(null, true);
    }

    @Test
    public void execute_templateContactedStatus_success() throws Exception {
        StorageStub storageStub = new StorageStub();
        ModelManager model = new ModelManager();
        TemplateCommand command = new TemplateCommand(Status.CONTACTED, false);
        
        CommandResult result = command.execute(model, storageStub);
        
        assertEquals(String.format(TemplateCommand.MESSAGE_OPEN_TEMPLATE_SUCCESS, Status.CONTACTED),
                result.getFeedbackToUser());
        assertTrue(result.isShowTemplate());
        assertEquals(Status.CONTACTED, model.getTemplateViewState().getStatus());
    }

    @Test
    public void execute_templateUncontactedStatus_success() throws Exception {
        StorageStub storageStub = new StorageStub();
        ModelManager model = new ModelManager();
        TemplateCommand command = new TemplateCommand(Status.UNCONTACTED, false);
        
        CommandResult result = command.execute(model, storageStub);
        
        assertEquals(String.format(TemplateCommand.MESSAGE_OPEN_TEMPLATE_SUCCESS, Status.UNCONTACTED),
                result.getFeedbackToUser());
        assertTrue(result.isShowTemplate());
        assertEquals(Status.UNCONTACTED, model.getTemplateViewState().getStatus());
    }

    @Test
    public void execute_saveTemplate_success() throws Exception {
        StorageStub storageStub = new StorageStub();
        ModelManager model = new ModelManager();
        
        // First open a template
        model.setTemplateViewState(Status.CONTACTED, "Test content");
        
        // Then save it
        TemplateCommand saveCommand = new TemplateCommand(null, true);
        CommandResult result = saveCommand.execute(model, storageStub);
        
        assertEquals(String.format(TemplateCommand.MESSAGE_SAVE_TEMPLATE_SUCCESS, Status.CONTACTED),
                result.getFeedbackToUser());
        assertFalse(result.isShowTemplate());
        assertEquals("Test content", storageStub.getSavedTemplate(Status.CONTACTED));
    }

    @Test
    public void execute_saveWithoutOpenTemplate_throwsCommandException() {
        StorageStub storageStub = new StorageStub();
        ModelManager model = new ModelManager();
        TemplateCommand saveCommand = new TemplateCommand(null, true);
        
        assertThrows(CommandException.class,
                TemplateCommand.MESSAGE_NO_TEMPLATE_TO_SAVE, () -> saveCommand.execute(model, storageStub));
    }

    @Test
    public void execute_allStatuses_success() throws Exception {
        StorageStub storageStub = new StorageStub();
        ModelManager model = new ModelManager();
        
        for (Status status : Status.values()) {
            TemplateCommand command = new TemplateCommand(status, false);
            CommandResult result = command.execute(model, storageStub);
            
            assertEquals(String.format(TemplateCommand.MESSAGE_OPEN_TEMPLATE_SUCCESS, status),
                    result.getFeedbackToUser());
            assertTrue(result.isShowTemplate());
            assertEquals(status, model.getTemplateViewState().getStatus());
        }
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        TemplateCommand command = new TemplateCommand(Status.CONTACTED, false);
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        TemplateCommand command1 = new TemplateCommand(Status.CONTACTED, false);
        TemplateCommand command2 = new TemplateCommand(Status.CONTACTED, false);
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentStatus_returnsFalse() {
        TemplateCommand command1 = new TemplateCommand(Status.CONTACTED, false);
        TemplateCommand command2 = new TemplateCommand(Status.UNCONTACTED, false);
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentSaveMode_returnsFalse() {
        TemplateCommand command1 = new TemplateCommand(Status.CONTACTED, false);
        TemplateCommand command2 = new TemplateCommand(Status.CONTACTED, true);
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_saveModeCommands_returnsTrue() {
        TemplateCommand command1 = new TemplateCommand(null, true);
        TemplateCommand command2 = new TemplateCommand(null, true);
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_null_returnsFalse() {
        TemplateCommand command = new TemplateCommand(Status.CONTACTED, false);
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        TemplateCommand command = new TemplateCommand(Status.CONTACTED, false);
        assertFalse(command.equals("string"));
    }

    @Test
    public void hashCode_sameValues_returnsSameHashCode() {
        TemplateCommand command1 = new TemplateCommand(Status.CONTACTED, false);
        TemplateCommand command2 = new TemplateCommand(Status.CONTACTED, false);
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void hashCode_differentValues_returnsDifferentHashCode() {
        TemplateCommand command1 = new TemplateCommand(Status.CONTACTED, false);
        TemplateCommand command2 = new TemplateCommand(Status.UNCONTACTED, false);
        assertNotEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void toString_validCommand_correctFormat() {
        TemplateCommand command = new TemplateCommand(Status.CONTACTED, false);
        String expected = "TemplateCommand{status=CONTACTED, isSave=false}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void toString_saveCommand_correctFormat() {
        TemplateCommand command = new TemplateCommand(null, true);
        String expected = "TemplateCommand{status=null, isSave=true}";
        assertEquals(expected, command.toString());
    }
}
