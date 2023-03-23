package com.me.ttrpg.dungeonworld.generator.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.me.util.utils.RollUtil;

@Component
public class OpenCityNpcGenerator extends AbstractNpcGenerator {

	@Override
	protected String generateRace(List<String> common, List<String> uncommon) {
		Map<List<String>, Double> probMap = new HashMap<>();
		probMap.put(common, .7);
		probMap.put(uncommon, .3);

		return RollUtil.randomElement(RollUtil.randomElement(probMap));
	}

}
