package com.me.ttrpg.dungeonworld.constant.steading;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.me.ttrpg.dungeonworld.dto.Steading;

public enum Prosperity implements SteadingTag {
	DIRT(0, "Nothing for sale, nobody has more than they need (and they're lucky if they have that). Unskilled labor is cheap."),
	POOR(1, "Only the bare necessities for sale. Weapons are scarce unless the steading is heavily defended or militant. Unskilled labor is readily available."),
	MODERATE(2, "Most mundane items are available. Some types of skilled laborers."),
	WEALTHY(3,
			"Any mundane item can be found for sale. Most kinds of skilled laborers are available, but demand is high for their time."),
	RICH(4, "Mundane items and more, if you know where to find them.	Specialist labor available, but at high prices.");

	private final int weight;
	private final String description;

	private Prosperity(final int weight, final String description) {
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

	private static final Map<Integer, Prosperity> WEIGHT_MAP = Stream.of(Prosperity.values())
			.collect(Collectors.toMap(Prosperity::getWeight, Function.identity()));

	public static void alter(final Steading steading, final int alter) {
		final int newWeight = Math.min(4, Math.max(0, steading.getProsperity().getWeight() + alter));
		steading.setProsperity(WEIGHT_MAP.get(newWeight));
	}
}
