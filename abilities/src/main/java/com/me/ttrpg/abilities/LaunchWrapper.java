package com.me.ttrpg.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.ttrpg.abilities.analysis.Analyzer;
import com.me.ttrpg.abilities.constants.AbilityScore;
import com.me.ttrpg.abilities.dto.AbilityArray;
import com.me.ttrpg.abilities.dto.ArrayOutputBlock;
import com.me.ttrpg.abilities.services.OutputService;
import com.me.util.spring.beans.BeanMapper;

@Component
public class LaunchWrapper {
	private static final List<AbilityScore> REFERENCE_ARRAY = Arrays.asList(AbilityScore._18, AbilityScore._16, AbilityScore._14, AbilityScore._12,
			AbilityScore._10, AbilityScore._8);

	@Autowired
	private BeanMapper beanMapper;
	@Autowired
	private OutputService outputService;

	public void launch() {
		final List<AbilityArray> abilityArrays = calculateAbilityArrays();
		final Set<String> analyzerNames = beanMapper.keySet(Analyzer.MAP_NAME);

		final Map<String, List<ArrayOutputBlock>> outputMap = new HashMap<>();
		outputMap.put("RAW",
				Arrays.asList(new ArrayOutputBlock("RAW", abilityArrays.stream().map(AbilityArray::convertToOutput).collect(Collectors.toList()))));

		for(final String analyzerName : analyzerNames) {
			final List<ArrayOutputBlock> analyzerOutput = beanMapper.getBean(Analyzer.class, Analyzer.MAP_NAME, analyzerName).analyze(abilityArrays);
			outputMap.put(analyzerName, analyzerOutput);
		}
		outputService.writeOutput(outputMap);
	}

	public List<AbilityArray> calculateAbilityArrays() {
		final List<AbilityArray> abilityArrays = new ArrayList<>();
		final AbilityScore[] allScores = AbilityScore.values();
		final int costAdjustor = calculateCostAdjustor();
		final int maxCost = calculateMaxCost(costAdjustor);

		for(int i1 = 0 ; i1 < allScores.length ; i1++) {
			final AbilityScore score1 = allScores[i1];
			final int cost1 = score1.getCost() + costAdjustor;
			if(cost1 > maxCost) {
				break;
			}
			for(int i2 = 0 ; i2 <= i1 ; i2++) {
				final AbilityScore score2 = allScores[i2];
				final int cost12 = cost1 + score2.getCost() + costAdjustor;
				if(cost12 > maxCost) {
					break;
				}
				for(int i3 = 0 ; i3 <= i2 ; i3++) {
					final AbilityScore score3 = allScores[i3];
					final int cost123 = cost12 + score3.getCost() + costAdjustor;
					if(cost123 > maxCost) {
						break;
					}
					for(int i4 = 0 ; i4 <= i3 ; i4++) {
						final AbilityScore score4 = allScores[i4];
						final int cost1234 = cost123 + score4.getCost() + costAdjustor;
						if(cost1234 > maxCost) {
							break;
						}
						for(int i5 = 0 ; i5 <= i4 ; i5++) {
							final AbilityScore score5 = allScores[i5];
							final int cost12345 = cost1234 + score5.getCost() + costAdjustor;
							if(cost12345 > maxCost) {
								break;
							}
							for(int i6 = 0 ; i6 <= i5 ; i6++) {
								final AbilityScore score6 = allScores[i6];
								final int cost123456 = cost12345 + score6.getCost() + costAdjustor;
								if(cost123456 > maxCost) {
									break;
								}
								final int pointsRemaining = maxCost - cost123456;
								final int costIncrease6 = allScores[i6 + 1].getCost() - score6.getCost();
								if(pointsRemaining < costIncrease6) {
									abilityArrays.add(AbilityArray.withScores(score1, score2, score3, score4, score5, score6));
									break;
								}
							}
						}
					}
				}
			}
		}
		return abilityArrays;
	}

	private int calculateCostAdjustor() {
		return 0 - AbilityScore.values()[0].getCost();
	}

	private int calculateMaxCost(final int costAdjustor) {
		return REFERENCE_ARRAY.stream().mapToInt(AbilityScore::getCost).map(c -> c + costAdjustor).sum();
	}

}
