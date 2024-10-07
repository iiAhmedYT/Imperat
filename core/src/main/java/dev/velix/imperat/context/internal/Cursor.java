package dev.velix.imperat.context.internal;

import dev.velix.imperat.context.Source;

import java.util.function.IntUnaryOperator;

public final class Cursor<S extends Source> {

    CommandInputStream<S> stream;
    int parameter, raw;

    Cursor(CommandInputStream<S> stream, int parameter, int raw) {
        this.stream = stream;
        this.parameter = parameter;
        this.raw = raw;
    }

    void shift(ShiftTarget shift, IntUnaryOperator operator) {
        switch (shift) {
            case RAW_ONLY -> this.raw = operator.applyAsInt(raw);
            case PARAMETER_ONLY -> this.parameter = operator.applyAsInt(parameter);
            default -> {
                this.raw = operator.applyAsInt(raw);
                this.parameter = operator.applyAsInt(parameter);
            }
        }
    }

    void shift(ShiftTarget target, ShiftOperation operation) {
        shift(target, operation.operator);
    }

    boolean canContinue(
        ShiftTarget target
    ) {
        return target.canContinue(this);
    }

    boolean isLast(ShiftTarget shiftTarget, int maxParams, int maxRaws) {
        if (shiftTarget == ShiftTarget.PARAMETER_ONLY)
            return parameter == maxParams - 1;
        else if (shiftTarget == ShiftTarget.RAW_ONLY)
            return raw == maxRaws - 1;
        else
            return parameter == maxParams - 1 && raw == maxRaws - 1;
    }

    boolean isLast(ShiftTarget shiftTarget) {
        return isLast(shiftTarget, stream.parametersLength(), stream.rawsLength());
    }

    int maxRaws() {
        return stream.rawsLength();
    }

    int maxParameters() {
        return stream.parametersLength();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Cursor<?> other)) return false;
        if (this.parameter != other.parameter) return false;
        return this.raw == other.raw;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + parameter;
        result = result * PRIME + raw;
        return result;
    }

}
