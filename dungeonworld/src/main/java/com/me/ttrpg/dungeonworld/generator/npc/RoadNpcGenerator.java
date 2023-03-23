package com.me.ttrpg.dungeonworld.generator.npc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.me.util.utils.RollUtil;

@Component
public class RoadNpcGenerator extends AbstractNpcGenerator {

	@Override
	protected String generateRace(List<String> common, List<String> uncommon) {
		List<String> allRaces = new ArrayList<>();
		allRaces.addAll(common);
		allRaces.addAll(uncommon);
		return RollUtil.randomElement(allRaces);
	}

}
