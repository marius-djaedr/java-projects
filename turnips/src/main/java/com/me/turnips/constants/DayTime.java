package com.me.turnips.constants;

import java.util.ArrayList;
import java.util.List;

public enum DayTime {
	SUNDAY("buy"),
	MONDAY_AM("sell_2"),
	MONDAY_PM("sell_3"),
	TUESDAY_AM("sell_4"),
	TUESDAY_PM("sell_5"),
	WEDNESDAY_AM("sell_6"),
	WEDNESDAY_PM("sell_7"),
	THURSDAY_AM("sell_8"),
	THURSDAY_PM("sell_9"),
	FRIDAY_AM("sell_10"),
	FRIDAY_PM("sell_11"),
	SATURDAY_AM("sell_12"),
	SATURDAY_PM("sell_13");

	private DayTime(final String htmlId) {
		this.htmlId = htmlId;
	}

	public final String htmlId;

	public static List<DayTime> after(final DayTime excluded) {
		final List<DayTime> list = new ArrayList<>();
		for(int i = excluded.ordinal() + 1 ; i < values().length ; i++) {
			list.add(values()[i]);
		}
		return list;
	}

}
