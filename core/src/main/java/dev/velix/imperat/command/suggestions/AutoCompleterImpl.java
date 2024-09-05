package dev.velix.imperat.command.suggestions;

import dev.velix.imperat.Imperat;
import dev.velix.imperat.command.Command;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.ArgumentQueue;
import dev.velix.imperat.context.Source;
import dev.velix.imperat.resolvers.PermissionResolver;
import dev.velix.imperat.resolvers.SuggestionResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ApiStatus.Internal
record AutoCompleterImpl<S extends Source>(Command<S> command) implements AutoCompleter<S> {

    private static @NotNull CompletionArg getLastArg(String[] args) {
        if (args.length == 0) return new CompletionArg(null, -1);
        int index = args.length - 1;
        String result = args[args.length - 1];
        if (result.isEmpty() || result.equals(" "))
            result = null;

        return new CompletionArg(result, index);
    }

    /**
     * @return The auto-completion command
     */
    @Override
    public Command<S> command() {
        return command;
    }

    /**
     * Autocompletes an argument from the whole position of the
     * argument-raw input
     *
     * @param dispatcher the command dispatcher
     * @param sender     the sender writing the command
     * @param args       the args for raw input
     * @return the auto-completed results
     */
    @Override
    public List<String> autoComplete(Imperat<S> dispatcher,
                                     S sender, String[] args) {
        CompletionArg argToComplete = getLastArg(args);
        return autoCompleteArgument(dispatcher, sender, argToComplete, args);
    }


    /**
     * Autocompletes an argument from the whole position of the
     * argument-raw input
     *
     * @param dispatcher the command dispatcher
     * @param sender     the sender of the auto-completion
     * @param currentArg the arg being completed
     * @param args       the args for raw input
     * @return the auto-completed results
     */
    @Override
    public List<String> autoCompleteArgument(Imperat<S> dispatcher,
                                             S sender,
                                             CompletionArg currentArg,
                                             String[] args) {


        final PermissionResolver<S> permResolver = dispatcher.getPermissionResolver();
        if (!command.isIgnoringACPerms() &&
                !permResolver.hasPermission(sender, command.getPermission())) {
            return Collections.emptyList();
        }

        ArgumentQueue queue = ArgumentQueue.parseAutoCompletion(args);
        var closestUsages = getClosestUsages(args);
        int index = currentArg.index();
        if (index == -1)
            index = 0;

        AutoCompleteList results = new AutoCompleteList();
        for (CommandUsage<S> usage : closestUsages) {
            if (index < 0 || index >= usage.getMaxLength()) continue;
            CommandParameter parameter = usage.getParameters().get(index);
            if (!command.isIgnoringACPerms() && !permResolver.hasPermission(sender, parameter.getPermission())) {
                continue;
            }
            if (parameter.isCommand()) {
                results.add(parameter.getName());
                parameter.asCommand().getAliases()
                        .forEach(results::add);
            } else {
                SuggestionResolver<S, ?> resolver = dispatcher.getParameterSuggestionResolver(parameter);
                if (resolver != null) {
                    results.addAll(resolver.autoComplete(command, sender,
                            queue, parameter, currentArg));
                }

            }

        }

        return new ArrayList<>(results.getResults());
    }


    private Collection<? extends CommandUsage<S>> getClosestUsages(String[] args) {

        return command
                .findUsages((usage) -> {
                    if (args.length >= usage.getMaxLength()) {
                        for (int i = 0; i < usage.getMaxLength(); i++) {
                            CommandParameter parameter = usage.getParameters().get(i);
                            if (!parameter.isCommand()) continue;

                            if (i >= args.length) return false;
                            String corresponding = args[i];
                            if (corresponding != null && !corresponding.isEmpty() &&
                                    !parameter.asCommand().hasName(corresponding))
                                return false;
                        }
                        return true;
                    }
                    return args.length <= usage.getMaxLength();
                });
    }


}
