package seedu.address.logic.files;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Allows production code to use the real filesystem while
 * tests can inject a fake implementation to simulate file presence and
 * contents without messing with real data.
 */
public interface FileSystemProvider {
    boolean exists(Path path);
    String readFile(Path path) throws IOException;
}
