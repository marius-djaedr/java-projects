package com.me.ttrpg.abilities.analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.me.ttrpg.abilities.dto.AbilityArray;
import com.me.util.annotations.MappableSingleton;

@MappableSingleton(mapName = Analyzer.MAP_NAME, key = "Score Standard Deviation")
public class SdScoreAnalyzer extends AbstractSimpleAnalyzer<BigDecimal> {

	@Override
	protected BigDecimal determineGroup(final AbilityArray array) {
		return array.getSdScores().setScale(0, RoundingMode.DOWN);
	}
}
