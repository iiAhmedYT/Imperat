package dev.velix.imperat.annotations.types;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

	/**
	 * @return The names of this command
	 * first element is the unique name of the command
	 * others are going to be considered the aliases
	 */
	@NotNull String[] value();
}
