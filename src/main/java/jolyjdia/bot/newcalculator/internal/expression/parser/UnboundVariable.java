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

package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.runtime.EvaluationException;
import jolyjdia.bot.newcalculator.internal.expression.runtime.LValue;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class UnboundVariable extends PseudoToken implements LValue {
    @NonNls
    private final String name;

    UnboundVariable(int position, String name) {
        super(position);
        this.name = name;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'V';
    }

    @Override
    @Contract(pure = true)
    public final @NotNull String toString() {
        return "UnboundVariable(" + name + ')';
    }

    @Contract(" -> fail")
    @Override
    public final double getValue() throws EvaluationException {
        throw new EvaluationException(getPosition(), "Tried to evaluate unbound variable!");
    }

    @Contract(" -> fail")
    @Override
    public final LValue optimize() throws EvaluationException {
        throw new EvaluationException(getPosition(), "Tried to optimize unbound variable!");
    }

    @Contract("_ -> fail")
    @Override
    public final double assign(double value) throws EvaluationException {
        throw new EvaluationException(getPosition(), "Tried to assign unbound variable!");
    }

    @Override
    public final @NotNull LValue bindVariables(@NotNull Expression expression) throws ParserException {
        final RValue variable = expression.getVariable(name);
        if (variable == null) {
            throw new ParserException(getPosition(), "Variable '" + name + "' not found");
        }

        return (LValue) variable;
    }

}
