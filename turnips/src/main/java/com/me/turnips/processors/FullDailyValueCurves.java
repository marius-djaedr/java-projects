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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
public class FullDailyValueCurves implements IEachElementCurveProcessor {
	private EnumMap<DayTime, Map<Integer, BigDecimal>> dailyValueMap;
	@Autowired
	private GoogleSheetsIo googleSheetsIo;

	@Override
	public void initialize(final UserInput userInput) {
		dailyValueMap = new EnumMap<>(DayTime.class);
	}

	@Override
	public void process(final CostCurve curve, final DayTime dayTime, final DailyCost curveCost) {
		final Map<Integer, BigDecimal> currentMap = dailyValueMap.getOrDefault(dayTime, new HashMap<>());
		dailyValueMap.put(dayTime, currentMap);
		Integer minVal = curveCost.getMinValue();
		Integer maxVal = curveCost.getMaxValue();
		if(minVal == null && maxVal == null) {
			return;
		} else if(minVal == null) {
			minVal = maxVal;
		} else if(maxVal == null) {
			maxVal = minVal;
		}

		final BigDecimal weight = curve.getProbability().divide(BigDecimal.valueOf(maxVal - minVal + 1), RoundingMode.HALF_UP);

		for(int i = minVal ; i <= maxVal ; i++) {
			final BigDecimal currentWeight = currentMap.getOrDefault(i, BigDecimal.ZERO);
			currentMap.put(i, currentWeight.add(weight));
		}
	}

	@Override
	public void output() {
		final int max = 1000;

		final List<List<String>> values = new ArrayList<>();
		final List<String> header = IntStream.rangeClosed(0, max).mapToObj(String::valueOf).collect(Collectors.toList());
		header.add(0, "");

		for(final DayTime dayTime : DayTime.values()) {
			values.add(Arrays.asList(dayTime.toString()));
			values.add(header);

			final List<String> probRow = new ArrayList<>();
			final List<String> cumuRow = new ArrayList<>();
			final List<String> expRow = new ArrayList<>();
			probRow.add("Probability");
			cumuRow.add("Cumulative");
			expRow.add("Expected");
			BigDecimal sum = BigDecimal.ZERO;
			BigDecimal expSum = BigDecimal.ZERO;
			final Map<Integer, BigDecimal> map = dailyValueMap.get(dayTime);
			for(int i = 0 ; i <= max ; i++) {
				final BigDecimal prob = map.getOrDefault(i, BigDecimal.ZERO);
				probRow.add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(prob));
				sum = sum.add(prob);
				cumuRow.add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(sum));
				expSum = expSum.add(prob.multiply(BigDecimal.valueOf(i)));
				expRow.add("");
			}
			expRow.set(expSum.intValue() + 1, "0");
			values.add(probRow);
			values.add(cumuRow);
			values.add(expRow);
		}

		try

		{
			googleSheetsIo.writeToSheet(values, TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "B" + (TurnipConstants.DATA_START_ROW + 10),
					false);
		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}
	}

}
