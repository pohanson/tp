package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.person.Status;

public class TemplateStorageManagerTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void readTemplate_nonExistentFile_returnsEmpty() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        Optional<String> result = storage.readTemplate(Status.CONTACTED);
        assertFalse(result.isPresent());
    }

    @Test
    public void readTemplate_existingFile_returnsContent() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String expectedContent = "Test template content";
        Path templateFile = temporaryFolder.resolve("contactedTemplate.txt");
        Files.writeString(templateFile, expectedContent);

        Optional<String> result = storage.readTemplate(Status.CONTACTED);
        assertTrue(result.isPresent());
        assertEquals(expectedContent, result.get());
    }

    @Test
    public void saveTemplate_newFile_createsFile() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String content = "New template";
        
        storage.saveTemplate(Status.CONTACTED, content);
        
        Path templateFile = temporaryFolder.resolve("contactedTemplate.txt");
        assertTrue(Files.exists(templateFile));
        assertEquals(content, Files.readString(templateFile));
    }

    @Test
    public void saveTemplate_existingFile_overwritesFile() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        Path templateFile = temporaryFolder.resolve("contactedTemplate.txt");
        Files.writeString(templateFile, "Old content");
        
        String newContent = "Updated content";
        storage.saveTemplate(Status.CONTACTED, newContent);
        
        assertEquals(newContent, Files.readString(templateFile));
    }

    @Test
    public void getDefaultTemplate_contacted_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.CONTACTED);
        assertTrue(template.contains("CONTACTED"));
        assertTrue(template.contains("contacted"));
    }

    @Test
    public void getDefaultTemplate_uncontacted_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.UNCONTACTED);
        assertTrue(template.contains("UNCONTACTED"));
        assertTrue(template.contains("uncontacted"));
    }

    @Test
    public void getDefaultTemplate_rejected_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.REJECTED);
        assertTrue(template.contains("REJECTED"));
        assertTrue(template.contains("rejected"));
    }

    @Test
    public void getDefaultTemplate_accepted_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.ACCEPTED);
        assertTrue(template.contains("ACCEPTED"));
        assertTrue(template.contains("accepted"));
    }

    @Test
    public void getDefaultTemplate_unreachable_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.UNREACHABLE);
        assertTrue(template.contains("UNREACHABLE"));
        assertTrue(template.contains("unreachable"));
    }

    @Test
    public void getDefaultTemplate_busy_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.BUSY);
        assertTrue(template.contains("BUSY"));
        assertTrue(template.contains("busy"));
    }

    @Test
    public void saveAndReadTemplate_multipleStatuses_worksCorrectly() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        
        storage.saveTemplate(Status.CONTACTED, "Contacted template");
        storage.saveTemplate(Status.REJECTED, "Rejected template");
        storage.saveTemplate(Status.ACCEPTED, "Accepted template");
        
        assertEquals("Contacted template", storage.readTemplate(Status.CONTACTED).get());
        assertEquals("Rejected template", storage.readTemplate(Status.REJECTED).get());
        assertEquals("Accepted template", storage.readTemplate(Status.ACCEPTED).get());
    }

    @Test
    public void saveTemplate_emptyContent_savesEmptyFile() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        
        storage.saveTemplate(Status.CONTACTED, "");
        
        Optional<String> result = storage.readTemplate(Status.CONTACTED);
        assertTrue(result.isPresent());
        assertEquals("", result.get());
    }

    @Test
    public void saveTemplate_multilineContent_preservesNewlines() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String multilineContent = "Line 1\nLine 2\nLine 3";
        
        storage.saveTemplate(Status.CONTACTED, multilineContent);
        
        Optional<String> result = storage.readTemplate(Status.CONTACTED);
        assertTrue(result.isPresent());
        assertEquals(multilineContent, result.get());
    }

    /**
     * A Storage stub that stores templates in memory for testing purposes.
     */
    public static class StorageStub implements TemplateStorage {
        private final Map<Status, String> templates = new HashMap<>();

        @Override
        public Optional<String> readTemplate(Status status) {
            return Optional.ofNullable(templates.get(status));
        }

        @Override
        public void saveTemplate(Status status, String content) {
            templates.put(status, content);
        }

        @Override
        public String getDefaultTemplate(Status status) {
            return "Default template for " + status;
        }

        public String getSavedTemplate(Status status) {
            return templates.get(status);
        }
    }
}
