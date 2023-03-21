package com.me.turnips.constants;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CurveType {
	UNKNOWN("I don't know", "unknown"),
	FLUCTUATING("Fluctuating", "fluctuating"),
	SMALL_SPIKE("Small Spike", "small-spike"),
	LARGE_SPIKE("Large Spike", "large-spike"),
	DECREASING("Decreasing", "decreasing");

	private CurveType(final String name, final String xpathPiece) {
		this.name = name;
		this.xpath = "//label[@for='pattern-radio-" + xpathPiece + "']";
	}

	private final String name;
	private final String xpath;
	private static final Map<String, CurveType> NAME_MAP = Stream.of(CurveType.values()).collect(Collectors.toMap(c -> c.name, Function.identity()));

	public static CurveType getByName(final String name) {
		return NAME_MAP.get(name);
	}

	public String getXpath() {
		return xpath;
	}
}
