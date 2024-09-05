package dev.velix.imperat.command.parameters;

import dev.velix.imperat.context.CommandFlag;
import dev.velix.imperat.context.CommandSwitch;
import dev.velix.imperat.context.Source;
import dev.velix.imperat.supplier.OptionalValueSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlagBuilder<S extends Source, T> extends ParameterBuilder<S, CommandFlag> {

    private final Class<?> inputType;
    private final List<String> aliases = new ArrayList<>();
    private OptionalValueSupplier<T> defaultValueSupplier = null;

    private FlagBuilder(String name, Class<T> inputType) {
        super(name, CommandFlag.class, true, false);
        this.inputType = inputType;
    }

    //for switches
    private FlagBuilder(String name) {
        this(name, null);
    }

    public static <S extends Source, T> FlagBuilder<S, T> ofFlag(String name, Class<T> inputType) {
        return new FlagBuilder<>(name, inputType);
    }

    public static <S extends Source, T> FlagBuilder<S, T> ofSwitch(String name) {
        return new FlagBuilder<>(name);
    }

    public FlagBuilder<S, T> aliases(List<String> aliases) {
        this.aliases.addAll(aliases);
        return this;
    }

    public FlagBuilder<S, T> aliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public FlagBuilder<S, T> flagDefaultInputValue(OptionalValueSupplier<T> valueSupplier) {
        if (inputType == null) {
            throw new IllegalArgumentException("Flag of type switches, cannot have a default value supplier !");
        }
        this.defaultValueSupplier = valueSupplier;
        return this;
    }

    @Override
    public FlagParameter build() {

        if (inputType != null) {
            CommandFlag flag = CommandFlag.create(name, aliases, inputType);
            return new FlagCommandParameter(flag, permission, description, defaultValueSupplier);
        } else {
            CommandSwitch commandSwitch = CommandSwitch.create(name, aliases);
            return new FlagCommandParameter(commandSwitch, permission, null);
        }
    }

}
