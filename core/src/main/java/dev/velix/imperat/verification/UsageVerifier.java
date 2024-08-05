package dev.velix.imperat.verification;

import dev.velix.imperat.command.CommandUsage;

/**
 * Verifies that the {@link CommandUsage} is suitable
 * to be added to the command
 *
 * <p>
 * Rule(s) to follow when creating a usage:
 * <p>
 * 1) A usage MUST have AT LEAST ONE required parameter at the beginning before any other
 * parameters whether optional or required
 * <p>
 * 2) If the usage doesn't have any subcommands,
 * it must not be duplicated in a similar parameters pattern
 * <p>
 * 3) It MUSTN'T have a greedy argument in the middle of the parameters,
 * therefore, if you want to add any greedy arguments,
 * you should put A SINGLE (not multiple) greedy argument
 * at the END of the usage parameters list ONLY
 *
 */
public interface UsageVerifier<C> {


	/**
	 * @param usage the usage
	 * @return Verifies the usage to be acceptable
	 */
	boolean verify(CommandUsage<C> usage);

	/**
	 * E
	 * @param usage1 the first usage
	 * @param usage2 the second usage
	 * @return whether both are usages similar and share similar indistinguishable parameters/syntax
	 */
	boolean areAmbiguous(CommandUsage<C> usage1, CommandUsage<C> usage2);

	static <C> UsageVerifier<C> defaultVerifier() {
		return new DefaultUsageVerifier<>();
	}

}
