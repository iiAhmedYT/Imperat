package dev.velix.imperat.command.tree;

import dev.velix.imperat.context.Source;
import dev.velix.imperat.util.CommandDebugger;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class CommandTreeVisualizer<S extends Source> {

    private final CommandTree<S> tree;

    CommandTreeVisualizer(CommandTree<S> tree) {
        this.tree = tree;
    }

    public static <S extends Source> CommandTreeVisualizer<S> of(CommandTree<S> tree) {
        return new CommandTreeVisualizer<>(tree);
    }

    public void visualize() {
        StringBuilder builder = new StringBuilder();
        visualizeNode(tree.root, builder, 0);
        CommandDebugger.debug(builder.toString());
    }

    private void visualizeNode(UsageNode<?> node, StringBuilder builder, int depth) {
        if (node == null) return;
        System.out.println("Checking node : " + node.format());
        builder.append("  ".repeat(Math.max(0, depth)));
        builder.append(node.format()).append("\n");
        for (UsageNode<?> child : node.getChildren()) {
            visualizeNode(child, builder, depth + 1);
        }

    }

}
