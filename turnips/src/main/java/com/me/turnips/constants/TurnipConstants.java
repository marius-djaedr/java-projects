package com.me.turnips.constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TurnipConstants {
	public static final Function<BigDecimal, String> BIG_DECIMAL_FORMATTER = b -> b == null ? "" : b.setScale(4, RoundingMode.HALF_UP).toString();
	public static final String SHEET_ID = "1nR_jLpr5AaZr9tbFVrzUEbRt4eijnc2-C_yiNrVsJOM";
	public static final String SHEET_NAME = "turnips!";
	public static final int DATA_START_ROW = 328;
	public static final List<String> DAY_TIME_HEADERS = Stream.of(DayTime.values()).map(String::valueOf).collect(Collectors.toList());
}
