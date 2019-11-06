package jolyjdia.bot.newcalculator.internal.expression.runtime;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Functions {

    private static final class Overload {
        private final Method method;
        private final int mask;
        private final boolean isSetter;

        /**
         * @param method
         * @throws IllegalArgumentException
         */
        private Overload(@NotNull Method method) {
            this.method = method;

            boolean issetter = false;
            int accum = 0;
            Class<?>[] parameters = method.getParameterTypes();
            for (Class<?> parameter : parameters) {
                if (issetter) {
                    throw new IllegalArgumentException("Method takes arguments that can't be cast to RValue.");
                }

                if (double.class.equals(parameter)) {
                    issetter = true;
                    continue;
                }
                if (!RValue.class.isAssignableFrom(parameter)) {
                    throw new IllegalArgumentException("Method takes arguments that can't be cast to RValue.");
                }
                accum <<= 2;

                accum |= LValue.class.isAssignableFrom(parameter) ? 3 : 1;
            }
            mask = accum;
            this.isSetter = issetter;
        }

        @Contract(pure = true)
        boolean matches(RValue... args) {
            if (this.isSetter) {
                return false;
            }
            if (this.method.getParameterTypes().length != args.length) { // TODO: optimize
                return false;
            }
            int accum = 0;
            for (RValue argument : args) {
                accum <<= 2;
                accum |= argument instanceof LValue ? 3 : 1;
            }
            return (accum & mask) == mask;
        }
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Function getFunction(int position, String name, RValue... args) {
        final Method getter = getMethod(name, args);
        return new Function(position, getter, args);
    }

    private static @Nullable Method getMethod(String name, RValue... args) {
        final List<Overload> overloads = functions.get(name);
        if (overloads == null) {
            return null;
        }
        for (Overload overload : overloads) {
            if (overload.matches(args)) {
                return overload.method;
            }
        }
        return null;
    }
    private static final Map<String, List<Overload>> functions = new HashMap<>();
    static {
        for (Method method : Functions.class.getMethods()) {
            try {
                addFunction(method);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    /**
     * @param method
     * @throws IllegalArgumentException
     */
    private static void addFunction(@NotNull Method method) {
        final String methodName = method.getName();

        Overload overload = new Overload(method);

        List<Overload> overloads = functions.computeIfAbsent(methodName, k -> new ArrayList<>());

        overloads.add(overload);
    }

    public static double sin(@NotNull RValue x) {
        return Math.sin(x.getValue());
    }

    public static double cos(@NotNull RValue x) {
        return Math.cos(x.getValue());
    }

    public static double tan(@NotNull RValue x) {
        return Math.tan(x.getValue());
    }


    public static double asin(@NotNull RValue x) {
        return Math.asin(x.getValue());
    }

    public static double acos(@NotNull RValue x) {
        return Math.acos(x.getValue());
    }

    public static double atan(@NotNull RValue x) {
        return Math.atan(x.getValue());
    }

    public static double atan2(@NotNull RValue y, @NotNull RValue x) {
        return Math.atan2(y.getValue(), x.getValue());
    }

    public static double sinh(@NotNull RValue x) {
        return Math.sinh(x.getValue());
    }

    public static double cosh(@NotNull RValue x) {
        return Math.cosh(x.getValue());
    }

    public static double tanh(@NotNull RValue x) {
        return Math.tanh(x.getValue());
    }

    public static double sqrt(@NotNull RValue x) {
        return Math.sqrt(x.getValue());
    }

    public static double cbrt(@NotNull RValue x) {
        return Math.cbrt(x.getValue());
    }

    public static double abs(@NotNull RValue x) {
        return Math.abs(x.getValue());
    }

    public static double min(@NotNull RValue a, @NotNull RValue b) {
        return Math.min(a.getValue(), b.getValue());
    }

    public static double min(@NotNull RValue a, @NotNull RValue b, @NotNull RValue c) {
        return Math.min(a.getValue(), Math.min(b.getValue(), c.getValue()));
    }
    public static double max(@NotNull RValue a, @NotNull RValue b) {
        return Math.max(a.getValue(), b.getValue());
    }

    public static double max(@NotNull RValue a, @NotNull RValue b, @NotNull RValue c) {
        return Math.max(a.getValue(), Math.max(b.getValue(), c.getValue()));
    }
    public static double ceil(@NotNull RValue x) {
        return Math.ceil(x.getValue());
    }

    public static double floor(@NotNull RValue x) {
        return Math.floor(x.getValue());
    }

    public static double rint(@NotNull RValue x) {
        return Math.rint(x.getValue());
    }

    public static double round(@NotNull RValue x) {
        return Math.round(x.getValue());
    }

    public static double exp(@NotNull RValue x) {
        return Math.exp(x.getValue());
    }

    public static double ln(@NotNull RValue x) {
        return Math.log(x.getValue());
    }

    public static double log(@NotNull RValue x) {
        return Math.log(x.getValue());
    }

    public static double log10(@NotNull RValue x) {
        return Math.log10(x.getValue());
    }


    public static double rotate(@NotNull LValue x, @NotNull LValue y, @NotNull RValue angle) {
        final double f = angle.getValue();

        final double cosF = Math.cos(f);
        final double sinF = Math.sin(f);

        final double xOld = x.getValue();
        final double yOld = y.getValue();

        x.assign(xOld * cosF - yOld * sinF);
        y.assign(xOld * sinF + yOld * cosF);

        return 0.0;
    }

    public static double swap(@NotNull LValue x, @NotNull LValue y) {
        final double tmp = x.getValue();

        x.assign(y.getValue());
        y.assign(tmp);

        return 0.0;
    }

    private final Map<Integer, double[]> megabuf = new HashMap<>();

    @Contract(pure = true)
    public Map<Integer, double[]> getMegabuf() {
        return megabuf;
    }

    private static double[] getSubBuffer(@NotNull Map<? super Integer, double[]> megabuf, Integer key) {
        return megabuf.computeIfAbsent(key, k -> new double[1024]);
    }

    private static double getBufferItem(final Map<? super Integer, double[]> megabuf, final int index) {
        return getSubBuffer(megabuf, index & -1024)[index & 1023];
    }

    private static double setBufferItem(final Map<? super Integer, double[]> megabuf, final int index, double value) {
        return getSubBuffer(megabuf, index & -1024)[index & 1023] = value;
    }

    private static double findClosest(Map<? super Integer, double[]> megabuf, double x, double y, double z, int index, int count, int stride) {
        int closestIndex = -1;
        double minDistanceSquared = Double.MAX_VALUE;

        for (int i = 0; i < count; ++i) {
            double currentX = getBufferItem(megabuf, index) - x;
            double currentY = getBufferItem(megabuf, index+1) - y;
            double currentZ = getBufferItem(megabuf, index+2) - z;

            double distanceSquared = currentX*currentX + currentY*currentY + currentZ*currentZ;

            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closestIndex = index;
            }

            index += stride;
        }

        return closestIndex;
    }
}
