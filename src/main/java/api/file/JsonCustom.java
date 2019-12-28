package api.file;

import com.google.gson.Gson;

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

    public final Gson getGson() {
        return gson;
    }

    public final void load(Type type) {
        try (FileInputStream fileInputStream = new FileInputStream(getFile());
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            this.gson.fromJson(inputStreamReader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void create() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print("{}");
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public final void save(Object object) {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print(gson.toJson(object));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}