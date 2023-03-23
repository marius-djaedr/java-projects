package com.me.util.functions;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts no input argument and returns no
 * result. Unlike most other functional interfaces, {@code VoidFunction} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept()}.
 *
 * @see Consumer
 */
@FunctionalInterface
public interface VoidFunction {

	/**
	 * Performs this operation.
	 *
	 */
	void accept();

	/**
	 * Returns a composed {@code VoidFunction} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code VoidFunction} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default VoidFunction andThen(final VoidFunction after) {
		Objects.requireNonNull(after);
		return () -> {
			accept();
			after.accept();
		};
	}

}
