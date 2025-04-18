package dev.velix.imperat.verification;

import dev.velix.imperat.command.Command;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.Source;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
class SimpleVerifier<S extends Source> implements UsageVerifier<S> {

    SimpleVerifier() {
    }

    @Override
    public boolean verify(CommandUsage<S> usage) {
        if (usage.getParameters().isEmpty()) {
            return true;
        }

        int greedyCount = 0;
        for (int i = 0; i < usage.getMaxLength(); i++) {
            CommandParameter<S> param = usage.getParameters().get(i);
            if (param.isGreedy()) greedyCount++;
        }

        if (greedyCount > 1) {
            return false;
        }

        CommandParameter<S> greedyParam = usage.getParameter(CommandParameter::isGreedy);
        if (greedyParam == null)
            return true;

        return greedyParam.position() == usage.getMaxLength() - 1;
    }

    @Override
    public boolean areAmbiguous(CommandUsage<S> firstUsage, CommandUsage<S> secondUsage) {
        //check length
        boolean sameLength = firstUsage.getMinLength() == secondUsage.getMinLength();
        boolean hasSubCommands = firstUsage.hasParamType(Command.class)
            && secondUsage.hasParamType(Command.class);

        if (sameLength && hasSubCommands) {
            List<CommandParameter<S>> parameterList1 = new ArrayList<>(firstUsage.getParameters());
            parameterList1.removeIf((param) -> !param.isCommand());

            List<CommandParameter<S>> parameterList2 = new ArrayList<>(secondUsage.getParameters());
            parameterList2.removeIf((param) -> !param.isCommand());

            return parameterList1.equals(parameterList2);
        }

        if (sameLength) {
            final int capacity = firstUsage.getMinLength();
            for (int i = 0; i < capacity; i++) {
                CommandParameter<S> firstUsageParameter = firstUsage.getParameter(i);
                CommandParameter<S> secondUsageParameter = secondUsage.getParameter(i);
                if (firstUsageParameter == null || secondUsageParameter == null) break;

                if ((firstUsageParameter.isCommand() && !secondUsageParameter.isCommand())
                    || (!firstUsageParameter.isCommand() && secondUsageParameter.isCommand())) {
                    return false;
                }
            }
        }

        return sameLength;
    }

}
