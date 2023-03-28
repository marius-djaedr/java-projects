package com.me.ttrpg.abilities.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.me.ttrpg.abilities.constants.AbilityScore;
import com.me.util.utils.StatUtil;

public class AbilityArray {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.000");
	private final List<AbilityScore> scores;
	private final int netCost, netMods;
	private final BigDecimal avgScores, sdScores, avgMods, sdMods;

	private AbilityArray(final List<AbilityScore> scores, final int netCost, final int netMods, final BigDecimal avgScores, final BigDecimal sdScores,
			final BigDecimal avgMods, final BigDecimal sdMods) {
		this.scores = scores;
		this.netCost = netCost;
		this.netMods = netMods;
		this.avgScores = avgScores;
		this.sdScores = sdScores;
		this.avgMods = avgMods;
		this.sdMods = sdMods;
	}

	public static AbilityArray withScores(final AbilityScore s1, final AbilityScore s2, final AbilityScore s3, final AbilityScore s4,
			final AbilityScore s5, final AbilityScore s6) {
		final List<AbilityScore> scores = Arrays.asList(s1, s2, s3, s4, s5, s6);

		int netCost = 0;
		int netMods = 0;
		int sumScores = 0;
		int sumScores2 = 0;
		int sumMods2 = 0;

		for(final AbilityScore scoreEnum : scores) {
			netCost += scoreEnum.getCost();

			final int score = scoreEnum.getScore();
			final int mod = scoreEnum.getMod();
			netMods += mod;
			sumScores += score;
			sumScores2 += score * score;
			sumMods2 += mod * mod;
		}
		final BigDecimal numScores = BigDecimal.valueOf(scores.size()).setScale(10);

		final BigDecimal avgScores = BigDecimal.valueOf(sumScores).setScale(10).divide(numScores, RoundingMode.HALF_UP);
		final BigDecimal avgMods = BigDecimal.valueOf(netMods).setScale(10).divide(numScores, RoundingMode.HALF_UP);

		final BigDecimal varScores = BigDecimal.valueOf(sumScores2).setScale(10).divide(numScores, RoundingMode.HALF_UP).subtract(avgScores.pow(2));
		final BigDecimal varMods = BigDecimal.valueOf(sumMods2).setScale(10).divide(numScores, RoundingMode.HALF_UP).subtract(avgMods.pow(2));

		final BigDecimal sdScores = StatUtil.sqrt(varScores, 10);
		final BigDecimal sdMods = StatUtil.sqrt(varMods, 10);

		return new AbilityArray(scores, netCost, netMods, avgScores, sdScores, avgMods, sdMods);
	}

	public List<AbilityScore> getScores() {
		return scores;
	}

	public int getNetCost() {
		return netCost;
	}

	public int getNetMods() {
		return netMods;
	}

	public BigDecimal getAvgScores() {
		return avgScores;
	}

	public BigDecimal getSdScores() {
		return sdScores;
	}

	public BigDecimal getAvgMods() {
		return avgMods;
	}

	public BigDecimal getSdMods() {
		return sdMods;
	}

	public ArrayOutputRow convertToOutput() {
		final List<String> scoreString = scores.stream().map(AbilityScore::getScore).map(String::valueOf).collect(Collectors.toList());
		return new ArrayOutputRow(scoreString, String.valueOf(netCost), String.valueOf(netMods), DECIMAL_FORMAT.format(avgScores),
				DECIMAL_FORMAT.format(sdScores), DECIMAL_FORMAT.format(avgMods), DECIMAL_FORMAT.format(sdMods));
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
