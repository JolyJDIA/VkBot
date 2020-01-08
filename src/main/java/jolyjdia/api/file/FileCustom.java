package jolyjdia.api.file;

import java.io.File;

public class FileCustom {
    private final File file;

    FileCustom(File file) {
        this.file = file;
    }

    public final File getFile() {
        return file;
    }
}
