package jolyjdia.api.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FileSerializer {

    private FileSerializer() {}

    public static void writeFile(String path, @NonNls String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(text + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void serialize(String path, Serializable obj) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static @Nullable Object deserialize(String path) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static final Pattern COMPILE = Pattern.compile("\\\\", Pattern.LITERAL);

    public static String checkSeparator(String url) {
        return COMPILE.matcher(url).replaceAll(Matcher.quoteReplacement(File.separator));
    }
}