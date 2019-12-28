package jolyjdia.bot.calculator.runtime;

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
              //  if (!RValue.class.isAssignableFrom(parameter)) {
              //      throw new IllegalArgumentException("Method takes arguments that can't be cast to RValue.");
             //   }
                accum <<= 2;

                accum |= LValue.class.isAssignableFrom(parameter) ? 3 : 1;
            }
            mask = accum;
            this.isSetter = issetter;
        }

        boolean matches(RValue... args) {
            if (this.isSetter) {
                return false;
            }
            if (this.method.getParameterTypes().length != args.length) {
                return false;
            }
            int accum = 0;
            for (RValue argument : args) {
                accum <<= 2;
                accum |= LValue.class.isAssignableFrom(argument.getClass()) ? 3 : 1;
            }
            return (accum & mask) == mask;
        }
    }

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
            List<Overload> overloads = functions.computeIfAbsent(method.getName(), k -> new ArrayList<>());
            overloads.add(new Overload(method));
        }
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
}
