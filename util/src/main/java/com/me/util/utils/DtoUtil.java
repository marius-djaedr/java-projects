package com.me.util.utils;

import java.util.Collection;
import java.util.function.Function;

public class DtoUtil {
	private DtoUtil() {}

	public static <T extends EmptyAble, O> T nullableTransform(final O other, final Function<O, T> transformer) {
		if(other == null) {
			return null;
		}

		final T transformed = transformer.apply(other);
		return transformed == null || transformed.isEmpty() ? null : transformed;
	}

	public static boolean isEmpty(final EmptyAble dto) {
		return dto == null || dto.isEmpty();
	}

	public static boolean isEmpty(final Collection<? extends EmptyAble> collection) {
		if(collection == null || collection.isEmpty()) {
			return true;
		}
		return collection.stream().allMatch(DtoUtil::isEmpty);
	}

	public interface EmptyAble {
		boolean isEmpty();
	}
}
