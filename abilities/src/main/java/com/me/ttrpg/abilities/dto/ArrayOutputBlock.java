package com.me.ttrpg.abilities.dto;

import java.util.List;

public class ArrayOutputBlock {
	private final String blockName;
	private final List<ArrayOutputRow> rows;

	public ArrayOutputBlock(final String blockName, final List<ArrayOutputRow> rows) {
		this.blockName = blockName;
		this.rows = rows;
	}

	public String getBlockName() {
		return blockName;
	}

	public List<ArrayOutputRow> getRows() {
		return rows;
	}

}
