package com.me.ttrpg.dungeonworld.generator.npc;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.me.ttrpg.dungeonworld.dto.Npc;
import com.me.ttrpg.dungeonworld.generator.DungeonWorldGenerator;
import com.me.ttrpg.dungeonworld.service.IConfigService;
import com.me.ttrpg.dungeonworld.service.IConfigService.ConfigListType;
import com.me.util.utils.RollUtil;

public abstract class AbstractNpcGenerator implements DungeonWorldGenerator<Npc> {
	@Autowired
	private IConfigService configService;

	@Override
	public Npc generate() {
		final List<String> namePrefixes = configService.getConfigList(ConfigListType.NPC_NAME_PREFIX);
		final List<String> nameMidfixes = configService.getConfigList(ConfigListType.NPC_NAME_MIDFIX);
		final List<String> nameSuffixes = configService.getConfigList(ConfigListType.NPC_NAME_SUFFIX);
		final List<String> commonRaces = configService.getConfigList(ConfigListType.NPC_RACE_COMMON);
		final List<String> uncommonRaces = configService.getConfigList(ConfigListType.NPC_RACE_UNCOMMON);
		final List<String> PRONOUNS = configService.getConfigList(ConfigListType.NPC_PRONOUNS);
		final List<String> CHARACTERISTICS = configService.getConfigList(ConfigListType.NPC_CHARACTERISTICS);
		final List<String> IDEALS = configService.getConfigList(ConfigListType.NPC_IDEALS);
		final List<String> FLAWS = configService.getConfigList(ConfigListType.NPC_FLAWS);
		final List<String> BONDS = configService.getConfigList(ConfigListType.NPC_BONDS);

		final Npc npc = new Npc();

		npc.setName(generateName(namePrefixes, nameMidfixes, nameSuffixes) + " " + generateName(namePrefixes, nameMidfixes, nameSuffixes));
		npc.setRace(generateRace(commonRaces, uncommonRaces));
		npc.setPronouns(RollUtil.randomElement(PRONOUNS));

		npc.setCharacteristic(RollUtil.randomElement(CHARACTERISTICS));
		npc.setIdeal("believes in " + RollUtil.randomElement(IDEALS));
		npc.setFlaw("is plagued by " + RollUtil.randomElement(FLAWS));
		IntStream.range(0, RollUtil.numToGenerate()).mapToObj(i -> "bonded to " + RollUtil.randomElement(BONDS)).forEach(npc::addBond);

		return npc;
	}

	private static String generateName(final List<String> namePrefixes, final List<String> nameMidfixes, final List<String> nameSuffixes) {
		return RollUtil.randomElement(namePrefixes) + RollUtil.randomElement(nameMidfixes) + RollUtil.randomElement(nameSuffixes);
	}

	protected abstract String generateRace(List<String> common, List<String> uncommon);
}
