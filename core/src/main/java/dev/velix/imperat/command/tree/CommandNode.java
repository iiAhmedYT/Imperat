package dev.velix.imperat.command.tree;

import dev.velix.imperat.command.Command;
import dev.velix.imperat.context.Source;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
final class CommandNode<S extends Source> extends UsageNode<Command<S>> {
    
    CommandNode(@NotNull Command<S> data) {
        super(data);
    }
    
    boolean isSubCommand() {
        return data.hasParent();
    }
    
    boolean isRoot() {
        return !isSubCommand();
    }
    
    @Override
    public boolean matchesInput(String raw) {
        return data.hasName(raw);
    }
    
    
    @Override
    public String format() {
        return data.format();
    }
    
    @Override
    public int priority() {
        return -1;
    }
    
}
