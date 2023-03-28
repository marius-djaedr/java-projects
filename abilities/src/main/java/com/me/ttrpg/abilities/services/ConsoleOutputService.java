package com.me.ttrpg.abilities.services;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.ttrpg.abilities.dto.ArrayOutputBlock;
import com.me.ttrpg.abilities.dto.ArrayOutputRow;

//@Service
public class ConsoleOutputService implements OutputService {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleOutputService.class);

	@Override
	public void writeOutput(final Map<String, List<ArrayOutputBlock>> outputMap) {
		final String header = buildHeader();
		for(final String groupName : outputMap.keySet()) {
			writeGroup(header, groupName, outputMap.get(groupName));
		}
	}

	private void writeGroup(final String header, final String groupName, final List<ArrayOutputBlock> outputBlock) {
		final StringJoiner sj = new StringJoiner("\n", "\n\n" + groupName + "\n", "\n");
		for(final ArrayOutputBlock block : outputBlock) {
			sj.add(block.getBlockName());
			sj.add(header);
			block.getRows().stream().map(this::buildRow).forEachOrdered(sj::add);
		}
		logger.info(sj.toString());
	}

	private String buildHeader() {
		return ArrayOutputRow.getHeaderFields().stream().map(s -> String.format("%10s", s)).collect(Collectors.joining(" "));
	}

	private String buildRow(final ArrayOutputRow row) {
		return row.getRowFields().stream().map(s -> String.format("%10s", s)).collect(Collectors.joining(" "));
	}
}
