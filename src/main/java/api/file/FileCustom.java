package api.file;

import org.jetbrains.annotations.Contract;

import java.io.File;

public class FileCustom {
    private final File file;

    @Contract(pure = true)
    FileCustom(File file) {
        this.file = file;
    }

    @Contract(pure = true)
    public final File getFile() {
        return file;
    }
}
