package dev.velix.imperat.caption.premade;

import dev.velix.imperat.Imperat;
import dev.velix.imperat.caption.Caption;
import dev.velix.imperat.caption.CaptionKey;
import dev.velix.imperat.caption.Messages;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.context.ResolvedContext;
import dev.velix.imperat.context.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoHelpPageCaption<S extends Source> implements Caption<S> {
    /**
     * @return the key
     */
    @Override
    public @NotNull CaptionKey getKey() {
        return CaptionKey.NO_HELP_PAGE_AVAILABLE_CAPTION;
    }

    /**
     * @param dispatcher the command dispatcher
     * @param context    the context
     * @param exception  the exception may be null if no exception provided
     * @return The message in the form of a component
     */
    @Override
    public @NotNull String getMessage(@NotNull Imperat<S> dispatcher,
                                      @NotNull Context<S> context,
                                      @Nullable Exception exception) {
        if (!(context instanceof ResolvedContext<S> resolvedContext) || resolvedContext.getDetectedUsage() == null
                || resolvedContext.getDetectedUsage().isHelp()) {
            throw new IllegalCallerException("Called NoHelpPageCaption in wrong the wrong sequence/part of the code");
        }

        int page = context.getArgumentOr("page", 1);
        return Messages.NO_HELP_PAGE_AVAILABLE.replace("<page>", String.valueOf(page));
    }
}
