package com.me.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtil {
	public static <K, V> String withFormatters(final Map<K, V> map, final Function<K, String> keyFormatter,
			final Function<V, String> valueFormatter) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> keyFormatter.apply(e.getKey()), e -> valueFormatter.apply(e.getValue())))
				.toString();
	}
}
