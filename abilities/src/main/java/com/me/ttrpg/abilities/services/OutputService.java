package com.me.ttrpg.abilities.services;

import java.util.List;
import java.util.Map;

import com.me.ttrpg.abilities.dto.ArrayOutputBlock;

public interface OutputService {
	void writeOutput(Map<String, List<ArrayOutputBlock>> outputMap);
}
