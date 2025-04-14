package com.me.ttrpg.dungeonworld.service;

import java.util.List;

public interface IConfigService {
	void reloadConfigs();

	List<String> getConfigList(ConfigListType type);

	String getConfigSingle(ConfigSingleType type);

	void updateSingleConfig(ConfigSingleType docId, String text);

	interface IConfigType {}

	enum ConfigSingleType implements IConfigType {
		DOC_ID;
	}

	enum ConfigListType implements IConfigType {
		STEADING_NAME_PREFIX,
		STEADING_NAME_SUFFIX,
		NPC_NAME_PREFIX,
		NPC_NAME_MIDFIX,
		NPC_NAME_SUFFIX,
		NPC_RACE_COMMON,
		NPC_RACE_UNCOMMON,
		NPC_PRONOUNS,
		NPC_CHARACTERISTICS,
		NPC_IDEALS,
		NPC_FLAWS,
		NPC_BONDS;
	}

}
