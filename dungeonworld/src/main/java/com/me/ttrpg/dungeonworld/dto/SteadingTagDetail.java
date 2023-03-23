package com.me.ttrpg.dungeonworld.dto;

import com.me.ttrpg.dungeonworld.constant.steading.SteadingTag;

public class SteadingTagDetail {
	private SteadingTag tag;
	private String detail;

	public SteadingTagDetail(final SteadingTag tag, final String detail) {
		this.tag = tag;
		this.detail = detail;
	}

	public SteadingTag getTag() {
		return tag;
	}

	public void setTag(final SteadingTag tag) {
		this.tag = tag;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(final String detail) {
		this.detail = detail;
	}

	// @formatter:off
	@Override
	public String toString() {
		String ret = "";
		if (detail != null) {
			ret = "(" + detail + ") ";
		}

		return ret + tag;
	}
}
