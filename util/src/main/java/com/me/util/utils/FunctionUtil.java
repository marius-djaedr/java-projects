package com.me.util.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionUtil {
	public static <T> Predicate<T> not(final Predicate<T> predicate) {
		return predicate.negate();
	}

	public static <T> Predicate<T> nullHandle(final Function<T, Boolean> function, final boolean nullBehavior) {
		return t -> {
			final Boolean bool = function.apply(t);
			return bool == null ? nullBehavior : bool;
		};
	}
}
