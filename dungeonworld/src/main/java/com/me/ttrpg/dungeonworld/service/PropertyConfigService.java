package com.me.ttrpg.dungeonworld.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyConfigService implements IConfigService {
	private static final Logger logger = LoggerFactory.getLogger(PropertyConfigService.class);
	private static final String CONFIG_LOC = "c://programs/dungeonworld/config.properties";

	@Autowired
	private Environment env;

	private Map<IConfigType, String> propertyKeyMap;
	private Map<ConfigListType, List<String>> configListMap;
	private Map<ConfigSingleType, String> configSingleMap;

	@PostConstruct
	private void setup() {
		propertyKeyMap = new HashMap<>();
		propertyKeyMap.put(ConfigListType.STEADING_NAME_PREFIX, "steading.name.prefixes");
		propertyKeyMap.put(ConfigListType.STEADING_NAME_SUFFIX, "steading.name.suffixes");
		propertyKeyMap.put(ConfigListType.NPC_NAME_PREFIX, "npc.name.prefixes");
		propertyKeyMap.put(ConfigListType.NPC_NAME_MIDFIX, "npc.name.midfixes");
		propertyKeyMap.put(ConfigListType.NPC_NAME_SUFFIX, "npc.name.suffixes");
		propertyKeyMap.put(ConfigListType.NPC_RACE_COMMON, "npc.race.common");
		propertyKeyMap.put(ConfigListType.NPC_RACE_UNCOMMON, "npc.race.uncommon");
		propertyKeyMap.put(ConfigListType.NPC_PRONOUNS, "npc.pronouns");
		propertyKeyMap.put(ConfigListType.NPC_CHARACTERISTICS, "npc.characteristics");
		propertyKeyMap.put(ConfigListType.NPC_IDEALS, "npc.ideals");
		propertyKeyMap.put(ConfigListType.NPC_FLAWS, "npc.flaws");
		propertyKeyMap.put(ConfigListType.NPC_BONDS, "npc.bonds");

		propertyKeyMap.put(ConfigSingleType.DOC_ID, "doc.id");
		reloadConfigs();
	}

	@Override
	public void reloadConfigs() {
		final Properties properties = readProperties();

		configListMap = new EnumMap<>(ConfigListType.class);
		for(final ConfigListType e : ConfigListType.values()) {
			putListPropertyOnMap(e, propertyKeyMap.get(e), properties);
		}

		configSingleMap = new EnumMap<>(ConfigSingleType.class);
		for(final ConfigSingleType e : ConfigSingleType.values()) {
			putSinglePropertyOnMap(e, propertyKeyMap.get(e), properties);
		}
	}

	@Override
	public List<String> getConfigList(final ConfigListType type) {
		return configListMap.get(type);
	}

	@Override
	public String getConfigSingle(final ConfigSingleType type) {
		return configSingleMap.get(type);
	}

	@Override
	public void updateSingleConfig(final ConfigSingleType type, final String text) {
		final Properties properties = readProperties();
		properties.setProperty(propertyKeyMap.get(type), text);
		writeProperties(properties);
	}

	private void putListPropertyOnMap(final ConfigListType type, final String propertyKey, final Properties properties) {
		final String value = properties.getProperty(propertyKey);

		final List<String> parsed = value == null ? new ArrayList<>() : Arrays.asList(value.split(","));
		configListMap.put(type, parsed);
	}

	private void putSinglePropertyOnMap(final ConfigSingleType type, final String propertyKey, final Properties properties) {
		final String value = properties.getProperty(propertyKey);
		final String parsed = value == null ? "" : value;
		configSingleMap.put(type, parsed);
	}

	private Properties readProperties() {
		final File file = new File(CONFIG_LOC);
		if(file.isFile()) {
			logger.info("Reading existing properties file " + file.getAbsolutePath());
			final Properties properties = new Properties();
			try(InputStream in = new FileInputStream(file)) {
				properties.load(in);
				return properties;
			} catch(final IOException e) {
				logger.error("Unable to read properties. Using default", e);
				return buildDefaultProperties();
			}
		} else {
			logger.info("No properties file found, generating from default: " + file.getAbsolutePath());
			final Properties properties = buildDefaultProperties();
			writeProperties(properties);
			return properties;
		}
	}

	private Properties buildDefaultProperties() {
		final Properties properties = new Properties();
		for(final String propertyKey : propertyKeyMap.values()) {
			final String value = env.getProperty(propertyKey, "");
			properties.setProperty(propertyKey, value);
		}
		return properties;
	}

	private void writeProperties(final Properties properties) {
		final File file = new File(CONFIG_LOC);
		logger.info("Writing to properties file " + file.getAbsolutePath());
		file.getParentFile().mkdirs();
		try(OutputStream out = new FileOutputStream(file)) {
			properties.store(out, null);
		} catch(final IOException e) {
			logger.error("Unable to write properties", e);
		}
	}
}
