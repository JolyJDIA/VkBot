package jolyjdia.bot.calculator.runtime;

import org.jetbrains.annotations.NotNull;

public final class Operators {

    private Operators() {}

    public static @NotNull Function getOperator(int position, String name, @NotNull RValue lhs, RValue rhs) throws NoSuchMethodException {
        if (LValue.class.isAssignableFrom(lhs.getClass())) {
            return new Function(position, Operators.class.getMethod(name, LValue.class, RValue.class), lhs, rhs);
        }
        return new Function(position, Operators.class.getMethod(name, RValue.class, RValue.class), lhs, rhs);
    }
    public static @NotNull Function getOperator(int position, String name, @NotNull RValue argument) throws NoSuchMethodException {
        if (LValue.class.isAssignableFrom(argument.getClass())) {
            return new Function(position, Operators.class.getMethod(name, LValue.class), argument);
        }
        return new Function(position, Operators.class.getMethod(name, RValue.class), argument);
    }
    public static double add(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() + rhs.getValue();
    }

    public static double sub(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() - rhs.getValue();
    }

    public static double mul(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() * rhs.getValue();
    }

    public static double div(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() / rhs.getValue();
    }

    public static double mod(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() % rhs.getValue();
    }

    public static double pow(@NotNull RValue lhs, @NotNull RValue rhs) {
        return Math.pow(lhs.getValue(), rhs.getValue());
    }

    public static double neg(@NotNull RValue x) {
        return -x.getValue();
    }

    public static double not(@NotNull RValue x) {
        return x.getValue() > 0.0 ? 0.0 : 1.0;
    }

    public static double inv(@NotNull RValue x) {
        return ~(long) x.getValue();
    }

    public static double lth(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() < rhs.getValue() ? 1.0 : 0.0;
    }

    public static double gth(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() > rhs.getValue() ? 1.0 : 0.0;
    }

    public static double leq(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() <= rhs.getValue() ? 1.0 : 0.0;
    }

    public static double geq(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() >= rhs.getValue() ? 1.0 : 0.0;
    }


    public static double equ(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() == rhs.getValue() ? 1.0 : 0.0;
    }

    public static double neq(@NotNull RValue lhs, @NotNull RValue rhs) {
        return lhs.getValue() == rhs.getValue() ? 0.0 : 1.0;
    }

    public static double near(@NotNull RValue lhs, @NotNull RValue rhs) {
        return almostEqual2sComplement(lhs.getValue(), rhs.getValue()) ? 1.0 : 0.0;
    }

    public static double or(@NotNull RValue lhs, RValue rhs) {
        return lhs.getValue() > 0.0 || rhs.getValue() > 0.0 ? 1.0 : 0.0;
    }

    public static double and(@NotNull RValue lhs, RValue rhs) {
        return lhs.getValue() > 0.0 && rhs.getValue() > 0.0 ? 1.0 : 0.0;
    }

    public static double ass(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(rhs.getValue());
    }

    public static double aadd(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(lhs.getValue() + rhs.getValue());
    }

    public static double asub(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(lhs.getValue() - rhs.getValue());
    }

    public static double amul(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(lhs.getValue() * rhs.getValue());
    }

    public static double adiv(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(lhs.getValue() / rhs.getValue());
    }

    public static double amod(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(lhs.getValue() % rhs.getValue());
    }

    public static double aexp(@NotNull LValue lhs, @NotNull RValue rhs) {
        return lhs.assign(Math.pow(lhs.getValue(), rhs.getValue()));
    }

    public static double inc(@NotNull LValue x) {
        return x.assign(x.getValue() + 1);
    }

    public static double dec(@NotNull LValue x) {
        return x.assign(x.getValue() - 1);
    }

    public static double postinc(@NotNull LValue x) {
        final double oldValue = x.getValue();
        x.assign(oldValue + 1);
        return oldValue;
    }

    public static double postdec(@NotNull LValue x) {
        final double oldValue = x.getValue();
        x.assign(oldValue - 1);
        return oldValue;
    }

    private static final double[] FACTORIALS = new double[171];
    static {
        double accum = 1;
        FACTORIALS[0] = 1;
        for (int i = 1; i < 171; ++i) {
            FACTORIALS[i] = accum *= i;
        }
    }

    public static double fac(@NotNull RValue x) {
        final int n = (int) x.getValue();

        if (n < 0) {
            return 0;
        }

        if (n >= FACTORIALS.length) {
            return Double.POSITIVE_INFINITY;
        }

        return FACTORIALS[n];
    }
    private static boolean almostEqual2sComplement(double a, double b) {
        long aLong = Double.doubleToRawLongBits(a);

        if (aLong < 0) aLong = 0x8000000000000000L - aLong;

        long bLong = Double.doubleToRawLongBits(b);
        if (bLong < 0) bLong = 0x8000000000000000L - bLong;

        final long longDiff = Math.abs(aLong - bLong);
        return longDiff <= 450359963;
    }

}
