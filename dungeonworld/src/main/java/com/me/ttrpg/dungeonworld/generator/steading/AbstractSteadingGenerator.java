package com.me.ttrpg.dungeonworld.generator.steading;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.me.ttrpg.dungeonworld.dto.Steading;
import com.me.ttrpg.dungeonworld.generator.DungeonWorldGenerator;
import com.me.util.utils.RollUtil;

public abstract class AbstractSteadingGenerator implements DungeonWorldGenerator<Steading> {

	@Value("#{'${steading.prefixes}'.split(',')}")
	private List<String> prefixes;
	@Value("#{'${steading.suffixes}'.split(',')}")
	private List<String> suffixes;

	public Steading generate() {
		final Steading steading = new Steading();
		String name = RollUtil.randomElement(prefixes) + RollUtil.randomElement(suffixes);
		steading.setName(name);
		mutateSteading(steading);
		return steading;
	}

	protected abstract void mutateSteading(Steading steading);
}
