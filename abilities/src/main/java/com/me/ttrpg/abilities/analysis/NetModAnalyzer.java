package com.me.ttrpg.abilities.analysis;

import com.me.ttrpg.abilities.dto.AbilityArray;
import com.me.util.annotations.MappableSingleton;

@MappableSingleton(mapName = Analyzer.MAP_NAME, key = "Net Modifier")
public class NetModAnalyzer extends AbstractSimpleAnalyzer<Integer> {

	@Override
	protected Integer determineGroup(final AbilityArray array) {
		return array.getNetMods();
	}
}
