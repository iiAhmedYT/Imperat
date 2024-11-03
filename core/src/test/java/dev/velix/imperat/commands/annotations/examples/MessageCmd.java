package dev.velix.imperat.commands.annotations.examples;

import dev.velix.imperat.TestSource;
import dev.velix.imperat.annotations.*;
import org.jetbrains.annotations.NotNull;

@Command({"message"})
public class MessageCmd {

    @Usage
    public void exec(@NotNull TestSource sender,
                     @Named("target") @NotNull String target,
                     @Named("message") @Suggest({"this is a long greedy", "some sentence", "idk"}) @Greedy String message) {
        sender.reply("sending to '" + target +
            "' the message '" + message + "'");
    }

}