package api.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class StringBind {
    @Contract(pure = true)
    private StringBind() {}

    @NotNull
    public static String toString(@NotNull String[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; ; ++i) {
            builder.append(a[i]);
            if (i == iMax) {
                return builder.toString();
            }
            builder.append(' ');
        }
    }
    /**
     * @param a
     * @return id
     * @throws NumberFormatException
     */
    public static int getIdNick(@NotNull String a) {
        return Integer.parseInt(a.substring(3).split("\\|")[0]);
    }

    /**
     * @param a
     * @return id
     * @throws NumberFormatException
     */
    public static int getIdString(String a) {
        return Integer.parseInt(a);
    }
}
