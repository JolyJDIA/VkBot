package jolyjdia.api.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jolyjdia.vk.api.queries.EnumParam;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.*;

public final class StringBind {
    public static final Gson GSON = new Gson();
    private StringBind() {}

    public static @NotNull String toString(int start, @NotNull String[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = start; ; ++i) {
            builder.append(a[i]);
            if (i == iMax) {
                return builder.toString();
            }
            builder.append(' ');
        }
    }
    @NotNull
    public static String toArray(@NotNull Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        if (!it.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(;;) {
            sb.append(it.next());
            if (!it.hasNext()) {
                return sb.toString();
            }
            sb.append(',');
        }
    }
    @SafeVarargs
    @NotNull
    public static <U> String toArray(@NotNull U... element) {
        int iMax = element.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; ; ++i) {
            builder.append(element[i]);
            if (i == iMax) {
                return builder.toString();
            }
            builder.append(',');
        }
    }
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static @Nullable List<String> substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == 0) {
            return Collections.emptyList();
        }
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            final int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }
    public static void log(@NonNls String msg) {
        try {
            System.out.println('[' +Thread.currentThread().getName()+"]: "+msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toArrayEnum(EnumParam... fields) {
        int iMax = fields.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0;; ++i) {
            builder.append(fields[i].getValue());
            if (i == iMax) {
                return builder.toString();
            }
            builder.append(',');
        }
    }

    public static String toArrayEnum(Collection<EnumParam> collection) {
        Iterator<EnumParam> it = collection.iterator();
        if (!it.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            sb.append(it.next().getValue());
            if (!it.hasNext()) {
                return sb.toString();
            }
            sb.append(',');
        }
    }
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(JsonElement json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static @NotNull String toString(@NotNull String[] a) {
        return toString(1, a);
    }
}
