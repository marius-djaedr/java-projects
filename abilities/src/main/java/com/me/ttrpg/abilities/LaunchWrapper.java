package com.me.ttrpg.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LaunchWrapper {
	private static final Logger logger = LoggerFactory.getLogger(LaunchWrapper.class);

	private static final int LOWER_BOUND = 6;
	private static final int UPPER_BOUND = 18;
	private static final int MAX_COST = 56;
	private static final Map<Integer, Integer> COST_MAP = buildCostMap();

	public void launch() {
		final List<List<Integer>> abilityArray = calculateAbilityArray();
		outputAbilityArray(abilityArray);
	}

	private static Map<Integer, Integer> buildCostMap() {
		final Map<Integer, Integer> costMap = new HashMap<>();
		costMap.put(6, 0);
		costMap.put(7, 1);
		costMap.put(8, 2);
		costMap.put(9, 3);
		costMap.put(10, 4);
		costMap.put(11, 5);
		costMap.put(12, 6);
		costMap.put(13, 7);
		costMap.put(14, 9);
		costMap.put(15, 11);
		costMap.put(16, 14);
		costMap.put(17, 17);
		costMap.put(18, 21);
		return costMap;
	}

	public List<List<Integer>> calculateAbilityArray() {
		final List<List<Integer>> abilityArray = new ArrayList<>();

		for(int score1 = LOWER_BOUND ; score1 <= UPPER_BOUND ; score1++) {
			final int cost1 = COST_MAP.get(score1);
			if(cost1 > MAX_COST) {
				break;
			}
			for(int score2 = LOWER_BOUND ; score2 <= score1 ; score2++) {
				final int cost12 = cost1 + COST_MAP.get(score2);
				if(cost12 > MAX_COST) {
					break;
				}
				for(int score3 = LOWER_BOUND ; score3 <= score2 ; score3++) {
					final int cost123 = cost12 + COST_MAP.get(score3);
					if(cost123 > MAX_COST) {
						break;
					}
					for(int score4 = LOWER_BOUND ; score4 <= score3 ; score4++) {
						final int cost1234 = cost123 + COST_MAP.get(score4);
						if(cost1234 > MAX_COST) {
							break;
						}
						for(int score5 = LOWER_BOUND ; score5 <= score4 ; score5++) {
							final int cost12345 = cost1234 + COST_MAP.get(score5);
							if(cost12345 > MAX_COST) {
								break;
							}
							for(int score6 = LOWER_BOUND ; score6 <= score5 ; score6++) {
								final int cost6 = COST_MAP.get(score6);
								final int cost123456 = cost12345 + cost6;
								if(cost123456 > MAX_COST) {
									break;
								}
								final int pointsRemaining = MAX_COST - cost123456;
								final int costIncrease6 = COST_MAP.get(score6 + 1) - cost6;
								if(pointsRemaining < costIncrease6) {
									abilityArray.add(Arrays.asList(score1, score2, score3, score4, score5, score6));
									break;
								}
							}
						}
					}
				}
			}
		}
		return abilityArray;
	}

	private void outputAbilityArray(final List<List<Integer>> abilityArray) {
		outputHeader();
		abilityArray.forEach(this::outputRow);
	}

	private void outputHeader() {
		// TODO Auto-generated method stub

	}

	private void outputRow(final List<Integer> row) {
		final String rowString = row.stream().map(i -> String.format("%2d", i)).collect(Collectors.joining(" "));
		logger.info("\n" + rowString);
	}
}
