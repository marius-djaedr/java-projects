package com.me.util.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupUtils {

	public static <E extends Enum<E>, K, V> Map<K, V> buildMapFromEnum(final Class<E> enumClass, final Function<E, K> keyFunc,
			final Function<E, V> valueFunc, final boolean includeNull) {
		return buildMapFromList(Arrays.asList(enumClass.getEnumConstants()), keyFunc, valueFunc, includeNull);
	}

	public static <E, K, V> Map<K, V> buildMapFromList(final List<? extends E> list, final Function<E, K> keyFunc, final Function<E, V> valueFunc,
			final boolean includeNull) {
		//		return list.stream().collect(Collectors.toMap(keyFunc, valueFunc, throwingMerger(), ConcurrentHashMap::new));
		//		return list.stream().collect(ConcurrentHashMap::new, (m, e) -> m.put(keyFunc.apply(e), valueFunc.apply(e)), (m1, m2) -> m1.putAll(m2));
		//		return list.stream().collect(HashMap::new, (m, e) -> m.put(keyFunc.apply(e), valueFunc.apply(e)), (m1, m2) -> m1.putAll(m2));
		return list.stream().filter(e -> includeNull || (keyFunc.apply(e) != null && valueFunc.apply(e) != null))
				.collect(Collectors.toMap(keyFunc, valueFunc));
	}

	public static <I, O> List<O> buildImmutableList(final List<I> listIn, final Function<I, O> conversion) {
		return Collections.unmodifiableList(listIn.stream().map(conversion).collect(Collectors.toList()));
	}

	/**
	 * copied from Collectors
	 * 
	 * Returns a merge function, suitable for use in
	 * {@link Map#merge(Object, Object, BiFunction) Map.merge()} or
	 * {@link #toMap(Function, Function, BinaryOperator) toMap()}, which always
	 * throws {@code IllegalStateException}.  This can be used to enforce the
	 * assumption that the elements being collected are distinct.
	 *
	 * @param <T> the type of input arguments to the merge function
	 * @return a merge function which always throw {@code IllegalStateException}
	 */
	private static <T> BinaryOperator<T> throwingMerger() {
		return (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
	}
}
