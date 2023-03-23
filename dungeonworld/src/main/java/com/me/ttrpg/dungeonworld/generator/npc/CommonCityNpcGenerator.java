package com.me.ttrpg.dungeonworld.generator.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.me.util.utils.RollUtil;

@Component
public class CommonCityNpcGenerator extends AbstractNpcGenerator {

	@Override
	protected String generateRace(List<String> common, List<String> uncommon) {
		Map<List<String>, Double> probMap = new HashMap<>();
		probMap.put(common, .9);
		probMap.put(uncommon, .1);

		return RollUtil.randomElement(RollUtil.randomElement(probMap));
	}

}
