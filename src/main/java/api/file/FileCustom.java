package api.file;

import org.jetbrains.annotations.Contract;

import java.io.File;

public abstract class FileCustom implements FileBuilder {
    private final File file;

    @Contract(pure = true)
    protected FileCustom(File file) {
        this.file = file;
    }

    @Contract(pure = true)
    public final File getFile() {
        return file;
    }

}
