package com.me.ttrpg.abilities.constants;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AbilityScore {
	//MUST STAY IN THIS ORDER
	_6(6, -2, -4),
	_7(7, -2, -3),
	_8(8, -1, -2),
	_9(9, -1, -1),
	_10(10, 0, 0),
	_11(11, 0, 1),
	_12(12, 1, 2),
	_13(13, 1, 3),
	_14(14, 2, 5),
	_15(15, 2, 7),
	_16(16, 3, 10),
	_17(17, 3, 13),
	_18(18, 4, 17);

	private AbilityScore(final int score, final int mod, final int cost) {
		this.score = score;
		this.mod = mod;
		this.cost = cost;
	}

	private final int score, mod, cost;

	private static final Map<Integer, AbilityScore> SCORE_MAP = Stream.of(AbilityScore.values())
			.collect(Collectors.toMap(e -> e.score, Function.identity()));

	public static AbilityScore forScore(final int score) {
		return SCORE_MAP.get(score);
	}

	public int getScore() {
		return score;
	}

	public int getMod() {
		return mod;
	}

	public int getCost() {
		return cost;
	}
}
