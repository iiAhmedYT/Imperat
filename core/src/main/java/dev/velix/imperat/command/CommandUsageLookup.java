package dev.velix.imperat.command;

import dev.velix.imperat.context.ArgumentQueue;
import dev.velix.imperat.context.Context;
import lombok.Data;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@ApiStatus.Internal
public final class CommandUsageLookup<C> {

	private final Command<C> primeCommand;

	CommandUsageLookup(Command<C> command) {
		this.primeCommand = command;
	}


	public SearchResult searchUsage(Context<C> context) {
		for (CommandUsage<C> commandUsage : primeCommand.getUsages()) {
			System.out.println("CHECKING " + CommandUsage.format(this.primeCommand, commandUsage));
			if (usageMatchesContext(context, commandUsage))
				return new SearchResult(commandUsage, Result.FOUND_COMPLETE);
			else if (commandUsage.hasParamType(Command.class) && checkResolvedLogic(context, commandUsage))
				return new SearchResult(commandUsage, Result.FOUND_INCOMPLETE);
		}

		return new SearchResult(null, Result.NOT_FOUND);
	}

	public List<CommandUsage<C>> findUsages(Predicate<CommandUsage<C>> predicate) {
		List<CommandUsage<C>> usages = new ArrayList<>();
		for (CommandUsage<C> usage : primeCommand.getUsages()) {
			if (predicate.test(usage)) {
				usages.add(usage);
			}
		}
		return usages;
	}

	private boolean usageMatchesContext(Context<C> context, CommandUsage<C> usage) {
		//1-arguments length check from both sides (raw and resolved)
		//2- compare raw and resolved parameters
		return checkLength(context.getArguments(), usage) && checkResolvedLogic(context, usage);
	}

	@SuppressWarnings("unchecked")
	private boolean checkResolvedLogic(Context<C> context,
	                                   CommandUsage<C> usage) {

		ArgumentQueue rawArgs = context.getArguments().copy();
		List<UsageParameter> parameters = usage.getParameters();

		int i = 0;
		while (!rawArgs.isEmpty()) {
			if (i >= parameters.size()) break;

			final String raw = rawArgs.poll();
			final UsageParameter parameter = parameters.get(i);

			if (parameter.isFlag())
				continue;

			if (parameter.isCommand()) {
				//the raw is the commandName
				Command<C> sub = (Command<C>) parameter;
				if (!sub.hasName(raw)) {
					return false;
				}

			}

			i++;
		}

		return true;
	}

	private boolean checkLength(ArgumentQueue rawArgs, CommandUsage<C> usage) {
		int rawLength = rawArgs.size();

		int maxExpectedLength = usage.getMaxLength();
		int minExpectedLength = usage.getMinLength();

		UsageParameter lastParameter = usage.getParameters().get(maxExpectedLength - 1);
		if (lastParameter.isGreedy()) {
			final int minMaxDiff = maxExpectedLength - minExpectedLength;
			int paramPos = lastParameter.getPosition() - minMaxDiff;
			rawLength = rawLength - (rawLength - paramPos - 1);
		}

		return rawLength >= minExpectedLength && rawLength <= maxExpectedLength;

	}

	@Data
	public final class SearchResult {
		private final CommandUsage<C> commandUsage;
		private final Result result;
	}

	public enum Result {

		NOT_FOUND,

		FOUND_INCOMPLETE,

		FOUND_COMPLETE

	}
}
