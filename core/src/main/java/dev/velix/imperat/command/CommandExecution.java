package dev.velix.imperat.command;

import dev.velix.imperat.CommandSource;
import dev.velix.imperat.context.ExecutionContext;

/**
 * This class represents the execution/action of this command that's triggered when
 * the sender asks for this command to be executed.
 *
 * @param <C> the command sender type
 */
public interface CommandExecution<C> {

	/**
	 * Executes the command's actions
	 * @param commandSource the source/sender of this command
	 * @param context the context of the command
	 */
	void execute(CommandSource<C> commandSource,
	             ExecutionContext context);

}
