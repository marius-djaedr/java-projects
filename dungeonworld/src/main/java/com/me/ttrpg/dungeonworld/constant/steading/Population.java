package com.me.ttrpg.dungeonworld.constant.steading;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.me.ttrpg.dungeonworld.dto.Steading;

public enum Population implements SteadingTag {
	EXODUS(0, "The steading has lost its population and is on the verge of collapse."),
	SHRINKING(1, "The population is less than it once was. Buildings stand empty."),
	STEADY(2, "The population is in line with the current size of the steading. Some slow growth."),
	GROWING(3, "More people than there are buildings."),
	BOOMING(4, "Resources are stretched thin trying to keep up with the number of people.");

	private final int weight;
	private final String description;

	private Population(final int weight, final String description) {
		this.weight = weight;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	private int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + description;
	}

	private static final Map<Integer, Population> WEIGHT_MAP = Stream.of(Population.values())
			.collect(Collectors.toMap(Population::getWeight, Function.identity()));

	public static void alter(final Steading steading, final int alter) {
		final int newWeight = Math.min(4, Math.max(0, steading.getPopulation().getWeight() + alter));
		steading.setPopulation(WEIGHT_MAP.get(newWeight));
	}
}
