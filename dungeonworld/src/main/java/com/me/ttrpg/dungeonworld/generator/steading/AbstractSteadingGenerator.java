package com.me.ttrpg.dungeonworld.generator.steading;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.me.ttrpg.dungeonworld.dto.Steading;
import com.me.ttrpg.dungeonworld.generator.DungeonWorldGenerator;
import com.me.ttrpg.dungeonworld.service.IConfigService;
import com.me.ttrpg.dungeonworld.service.IConfigService.ConfigListType;
import com.me.util.utils.RollUtil;

public abstract class AbstractSteadingGenerator implements DungeonWorldGenerator<Steading> {

	@Autowired
	private IConfigService configService;

	@Override
	public Steading generate() {
		final Steading steading = new Steading();

		final List<String> prefixes = configService.getConfigList(ConfigListType.STEADING_NAME_PREFIX);
		final List<String> suffixes = configService.getConfigList(ConfigListType.STEADING_NAME_SUFFIX);
		final String name = RollUtil.randomElement(prefixes) + RollUtil.randomElement(suffixes);
		steading.setName(name);

		mutateSteading(steading);
		return steading;
	}

	protected abstract void mutateSteading(Steading steading);
}
