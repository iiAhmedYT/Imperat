package dev.velix.imperat.resolvers;

import dev.velix.imperat.context.Context;
import dev.velix.imperat.context.Source;
import dev.velix.imperat.exception.ImperatException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.function.Supplier;

/**
 * Resolves a default non-overridable value for
 * missing required arguments
 *
 * @param <T> the type of resolver's parameter
 */
public interface ContextResolver<S extends Source, T> {

    //TODO make a registry for custom annotations that are used in the parameters
    //TODO and then replace `Parameter` with `ParameterCommandElement` with added method `hasCustomAnnotation`

    static <S extends Source, T> ContextResolver<S, T> of(T value) {
        return (c, p) -> value;
    }

    static <S extends Source, T> ContextResolver<S, T> of(Supplier<T> supplier) {
        return of(supplier.get());
    }

    /**
     * Resolves a parameter's default value
     * if it has been not input by the user
     *
     * @param context   the context
     * @param parameter the parameter (null if used the classic way)
     * @return the resolved default-value
     */
    @Nullable
    T resolve(
            @NotNull Context<S> context,
            @Nullable Parameter parameter
    ) throws ImperatException;

}
