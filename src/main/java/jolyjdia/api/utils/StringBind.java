package jolyjdia.api.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StringBind {

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

    public static @NotNull String toString(@NotNull String[] a) {
        return toString(1, a);
    }
}
