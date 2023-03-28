package com.me.ttrpg.abilities.analysis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.me.ttrpg.abilities.dto.AbilityArray;
import com.me.ttrpg.abilities.dto.ArrayOutputBlock;
import com.me.ttrpg.abilities.dto.ArrayOutputRow;

public abstract class AbstractSimpleAnalyzer<T extends Comparable<T>> implements Analyzer {

	@Override
	public List<ArrayOutputBlock> analyze(final List<AbilityArray> arrays) {
		final Map<T, List<ArrayOutputRow>> groupMap = arrays.stream()
				.collect(Collectors.groupingBy(this::determineGroup, Collectors.mapping(AbilityArray::convertToOutput, Collectors.toList())));

		return groupMap.keySet().stream().sorted().map(t -> new ArrayOutputBlock(String.valueOf(t), groupMap.get(t))).collect(Collectors.toList());
	}

	protected abstract T determineGroup(AbilityArray array);
}
