package com.me.ttrpg.abilities.dto;

import java.util.Arrays;
import java.util.List;

public class ArrayOutputRow {
	//TODO somehow make this agnostic to number of scores
	private final String score1, score2, score3, score4, score5, score6;
	private final String netCost, netModifiers;
	private final String averageScores, standardDeviationScores, averageModifiers, standardDeviationModifiers;

	ArrayOutputRow(final List<String> scores, final String netCost, final String netModifiers, final String averageScores,
			final String standardDeviationScores, final String averageModifiers, final String standardDeviationModifiers) {
		this.score1 = scores.get(0);
		this.score2 = scores.get(1);
		this.score3 = scores.get(2);
		this.score4 = scores.get(3);
		this.score5 = scores.get(4);
		this.score6 = scores.get(5);
		this.netCost = netCost;
		this.netModifiers = netModifiers;
		this.averageScores = averageScores;
		this.standardDeviationScores = standardDeviationScores;
		this.averageModifiers = averageModifiers;
		this.standardDeviationModifiers = standardDeviationModifiers;
	}

	//TODO build these with reflection
	public static List<String> getHeaderFields() {
		return Arrays.asList("Score 1", "Score 2", "Score 3", "Score 4", "Score 5", "Score 6", "Cost Sum", "Mod Sum", "Score Avg", "Score SD",
				"Mod Avg", "Mod SD");
	}

	public List<String> getRowFields() {
		return Arrays.asList(score1, score2, score3, score4, score5, score6, netCost, netModifiers, averageScores, standardDeviationScores,
				averageModifiers, standardDeviationModifiers);
	}
}
