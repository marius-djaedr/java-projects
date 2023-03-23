package com.me.ttrpg.dungeonworld.constant.steading;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.me.ttrpg.dungeonworld.dto.Steading;

public enum Defense implements SteadingTag {
	NONE(0, "Clubs, torches, farming tools."),
	MILITIA(1, "There are able-bodied men and women with worn weapons ready to be called, but no standing force."),
	WATCH(2, "There are a few watchers posted who look out for trouble and settle small problems, but their main role is to summon the militia."),
	GUARD(3, "There are armed defenders at all times with a total pool of less than 100 (or equivalent). There is always at least one armed patrol about the steading."),
	GARRISON(4,
			"There are armed defenders at all times with a total pool of 100-300 (or equivalent). There are multiple armed patrols at all times."),
	BATTALION(5,
			"As many as 1,000 armed defenders (or equivalent). The steading has manned maintained defenses as well."),
	LEGION(6,
			"The steading is defended by thousands of armed soldiers (or equivalent). The steading's defenses are intimidating.");

	private final int weight;
	private final String description;

	private Defense(final int weight, final String description) {
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

	private static final Map<Integer, Defense> WEIGHT_MAP = Stream.of(Defense.values())
			.collect(Collectors.toMap(Defense::getWeight, Function.identity()));

	public static void alter(final Steading steading, final int alter) {
		final int newWeight = Math.min(6, Math.max(0, steading.getDefense().getWeight() + alter));
		steading.setDefense(WEIGHT_MAP.get(newWeight));
	}
}
