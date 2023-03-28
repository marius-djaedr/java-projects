package com.me.ttrpg.abilities.analysis;

import java.util.List;

import com.me.ttrpg.abilities.dto.AbilityArray;
import com.me.ttrpg.abilities.dto.ArrayOutputBlock;

public interface Analyzer {
	String MAP_NAME = "ANALYZER_MAP_NAME";

	List<ArrayOutputBlock> analyze(List<AbilityArray> arrays);
}
