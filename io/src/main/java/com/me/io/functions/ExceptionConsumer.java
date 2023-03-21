package com.me.io.functions;

import java.util.Objects;
import java.util.function.Consumer;

import com.me.io.exceptions.FunctionException;

public interface ExceptionConsumer<T> extends Consumer<T> {

	void acceptWithException(T t) throws Exception;

	@Override
	default void accept(final T t) {
		try {
			acceptWithException(t);
		} catch(final Exception e) {
			throw new FunctionException(e);
		}
	}

	@Override
	default ExceptionConsumer<T> andThen(final Consumer<? super T> after) {
		Objects.requireNonNull(after);
		return (final T t) -> {
			acceptWithException(t);
			after.accept(t);
		};
	}

	default ExceptionConsumer<T> andThen(final ExceptionConsumer<? super T> after) {
		Objects.requireNonNull(after);
		return (final T t) -> {
			acceptWithException(t);
			after.acceptWithException(t);
		};
	}

}
