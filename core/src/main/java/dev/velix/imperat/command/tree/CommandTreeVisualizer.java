package dev.velix.imperat.command.tree;

import dev.velix.imperat.context.Source;
import dev.velix.imperat.util.CommandDebugger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class CommandTreeVisualizer<S extends Source> {

    private final @Nullable CommandTree<S> tree;

    CommandTreeVisualizer(@Nullable CommandTree<S> tree) {
        this.tree = tree;
    }

    public static <S extends Source> CommandTreeVisualizer<S> of(@Nullable CommandTree<S> tree) {
        return new CommandTreeVisualizer<>(tree);
    }


    public void visualize() {
        if (tree == null) return;
        StringBuilder builder = new StringBuilder();
        visualizeNode(tree.root, builder, 0);
        CommandDebugger.debug(builder.toString());
    }

    private void visualizeNode(UsageNode<?> node, StringBuilder builder, int depth) {
        if (node == null) return;
        builder.append("  ".repeat(Math.max(0, depth)));
        builder.append(node.format()).append("\n");
        for (UsageNode<?> child : node.getChildren()) {
            visualizeNode(child, builder, depth + 1);
        }

    }

}
