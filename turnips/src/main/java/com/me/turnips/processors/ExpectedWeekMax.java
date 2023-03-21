package com.me.turnips.processors;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mortbay.io.RuntimeIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.io.google.GoogleSheetsIo;
import com.me.turnips.constants.DayTime;
import com.me.turnips.constants.TurnipConstants;
import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.DailyCost;
import com.me.turnips.dtos.UserInput;

@Component
public class ExpectedWeekMax implements IEachElementCurveProcessor {
	private Map<CostCurve, BigDecimal> curveMax;
	private Map<CostCurve, List<DayTime>> curveMaxOccurrences;
	@Autowired
	private GoogleSheetsIo googleSheetsIo;

	@Override
	public void initialize(final UserInput userInput) {
		curveMax = new HashMap<>();
		curveMaxOccurrences = new HashMap<>();
	}

	@Override
	public void process(final CostCurve curve, final DayTime dayTime, final DailyCost curveCost) {
		final BigDecimal average = curveCost.average();

		BigDecimal currentMax = curveMax.getOrDefault(curve, BigDecimal.ZERO);
		List<DayTime> currentOccurrences = curveMaxOccurrences.getOrDefault(curve, new ArrayList<>());
		if(average != null && average.compareTo(currentMax) >= 0) {
			if(average.compareTo(currentMax) > 0) {
				currentMax = average;
				currentOccurrences = new ArrayList<>();
			}
			currentOccurrences.add(dayTime);
		}
		curveMax.put(curve, currentMax);
		curveMaxOccurrences.put(curve, currentOccurrences);
	}

	@Override
	public void output() {
		BigDecimal expectedMax = BigDecimal.ZERO;
		final EnumMap<DayTime, BigDecimal> expectedDayMap = new EnumMap<>(DayTime.class);
		for(final CostCurve curve : curveMax.keySet()) {
			final BigDecimal prob = curve.getProbability();
			expectedMax = expectedMax.add(curveMax.get(curve).multiply(prob));

			final List<DayTime> occurrences = curveMaxOccurrences.get(curve);
			final BigDecimal weight = prob.divide(BigDecimal.valueOf(occurrences.size()), RoundingMode.HALF_UP);
			for(final DayTime occurrence : occurrences) {
				final BigDecimal current = expectedDayMap.getOrDefault(occurrence, BigDecimal.ZERO);
				expectedDayMap.put(occurrence, current.add(weight));
			}
		}

		BigDecimal expectedOccurrenceSum = BigDecimal.ZERO;
		BigDecimal cumuSum = BigDecimal.ZERO;
		final EnumMap<DayTime, BigDecimal> cumuDayMap = new EnumMap<>(DayTime.class);
		int dayTimeInt = 0;
		for(final DayTime dayTime : DayTime.values()) {
			cumuSum = cumuSum.add(expectedDayMap.getOrDefault(dayTime, BigDecimal.ZERO));
			cumuDayMap.put(dayTime, cumuSum);
			expectedOccurrenceSum = expectedOccurrenceSum
					.add(expectedDayMap.getOrDefault(dayTime, BigDecimal.ZERO).multiply(BigDecimal.valueOf(dayTimeInt++)));
		}

		final List<List<String>> values = Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		values.get(0).add("");
		values.get(1).add("Probability");
		values.get(2).add("Cumulative");
		values.get(3).add("Expected");
		for(final DayTime dayTime : DayTime.values()) {
			values.get(0).add(dayTime.toString());
			if(expectedDayMap.get(dayTime) == null) {
				values.get(1).add("0");
			} else {
				values.get(1).add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(expectedDayMap.get(dayTime)));
			}
			values.get(2).add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(cumuDayMap.get(dayTime)));
			values.get(3).add("");
		}
		values.get(3).set(expectedOccurrenceSum.intValue() + 2, "0");

		try {
			googleSheetsIo.writeToSheet(Arrays.asList(Arrays.asList(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(expectedMax))),
					TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "B18", false);

			googleSheetsIo.writeToSheet(values, TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "B" + (TurnipConstants.DATA_START_ROW + 5),
					false);
		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}

	}

}
