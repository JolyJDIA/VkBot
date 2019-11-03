/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package jolyjdia.bot.newcalculator.internal.runtime;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Operators {

    @Contract(pure = true)
    private Operators() {}

    @Contract("_, _, null, _ -> new")
    public static @NotNull Function getOperator(int position, String name, RValue lhs, RValue rhs) throws NoSuchMethodException {
        if (lhs instanceof LValue) {
            try {
                return new Function(position, Operators.class.getMethod(name, LValue.class, RValue.class), lhs, rhs);
            } catch (NoSuchMethodException ignored) { }
        }
        return new Function(position, Operators.class.getMethod(name, RValue.class, RValue.class), lhs, rhs);
    }

    @Contract("_, _, null -> new")
    public static @NotNull Function getOperator(int position, String name, RValue argument) throws NoSuchMethodException {
        if (argument instanceof LValue) {
            try {
                return new Function(position, Operators.class.getMethod(name, LValue.class), argument);
            } catch (NoSuchMethodException ignored) { }
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


    private static final double[] factorials = new double[171];
    static {
        double accum = 1;
        factorials[0] = 1;
        for (int i = 1; i < factorials.length; ++i) {
            factorials[i] = accum *= i;
        }
    }

    public static double fac(@NotNull RValue x) {
        final int n = (int) x.getValue();

        if (n < 0) {
            return 0;
        }

        if (n >= factorials.length) {
            return Double.POSITIVE_INFINITY;
        }

        return factorials[n];
    }
    // Usable AlmostEqual function, based on http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm
    private static boolean almostEqual2sComplement(double a, double b) {
        long aLong = Double.doubleToRawLongBits(a);

        if (aLong < 0) aLong = 0x8000000000000000L - aLong;

        long bLong = Double.doubleToRawLongBits(b);
        if (bLong < 0) bLong = 0x8000000000000000L - bLong;

        final long longDiff = Math.abs(aLong - bLong);
        return longDiff <= 450359963;
    }

}
