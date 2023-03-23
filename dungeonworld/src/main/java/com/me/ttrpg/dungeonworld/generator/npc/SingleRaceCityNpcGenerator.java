package com.me.ttrpg.dungeonworld.generator.npc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.me.util.utils.RollUtil;

@Component
public class SingleRaceCityNpcGenerator extends AbstractNpcGenerator {

	@Override
	protected String generateRace(List<String> common, List<String> uncommon) {
		Map<List<String>, Double> probMap = new HashMap<>();
		probMap.put(Arrays.asList("SINGLE RACE"), .9);
		probMap.put(common, .09);
		probMap.put(uncommon, .01);

		return RollUtil.randomElement(RollUtil.randomElement(probMap));
	}

}
