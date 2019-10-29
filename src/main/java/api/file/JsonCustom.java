package api.file;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonCustom extends FileCustom {
    private Gson gson;

    protected JsonCustom(File file) {
        super(file);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.length() == 0) {
            this.create();
        }
    }
    public final void setGson(Gson gson) {
        this.gson = gson;
    }

    @Contract(pure = true)
    public final Gson getGson() {
        return gson;
    }

    public final void load(Type type) {
        try (FileInputStream fileInputStream = new FileInputStream(getFile());
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            this.gson.fromJson(inputStreamReader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void create() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print("{}");
            pw.flush();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public final void save(Object object) {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print(gson.toJson(object));
            pw.flush();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static class MyExclusionStrategy implements ExclusionStrategy {
        @Override
        public final boolean shouldSkipField(@NotNull FieldAttributes f) {
            return f.getAnnotation(Expose.class) != null;
        }

        @Contract(pure = true)
        @Override
        public final boolean shouldSkipClass(Class<?> c) {
            return false;
        }
    }
}