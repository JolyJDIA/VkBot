package api.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class FileSerializer {
    @Contract(pure = true)
    private FileSerializer() {}

    public static void writeFile(String path, @NonNls String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(text + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}