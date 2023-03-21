package com.me.turnips.processors;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

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
public class BasicCurveProcessor implements IEachElementCurveProcessor {
	@Autowired
	private GoogleSheetsIo googleSheetsIo;

	private EnumMap<DayTime, DailyCost> aggregateMap;
	private EnumMap<DayTime, BigDecimal> expMap;

	@Override
	public void initialize(final UserInput userInput) {
		aggregateMap = new EnumMap<>(DayTime.class);
		expMap = new EnumMap<>(DayTime.class);
	}

	@Override
	public void process(final CostCurve curve, final DayTime dayTime, final DailyCost curveCost) {
		final BigDecimal prob = curve.getProbability();

		final DailyCost currentAgg = aggregateMap.getOrDefault(dayTime, DailyCost.forAgg());
		currentAgg.setMinValue(nullMin(currentAgg.getMinValue(), curveCost.getMinValue()));
		currentAgg.setMaxValue(nullMax(currentAgg.getMaxValue(), curveCost.getMaxValue()));
		aggregateMap.put(dayTime, currentAgg);

		final BigDecimal currentExp = expMap.getOrDefault(dayTime, BigDecimal.ZERO);
		BigDecimal avgCost = curveCost.average();
		if(avgCost == null) {
			avgCost = BigDecimal.ZERO;
		}
		final BigDecimal augend = avgCost.multiply(prob);
		expMap.put(dayTime, currentExp.add(augend));
	}

	@Override
	public void output() {
		final List<List<String>> values = Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		values.get(0).add("");
		values.get(1).add("Expected Value");
		values.get(2).add("Min");
		values.get(3).add("Max");
		for(final DayTime dayTime : DayTime.values()) {
			values.get(0).add(dayTime.toString());
			values.get(1).add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(expMap.get(dayTime)));
			values.get(2).add(aggregateMap.get(dayTime).getMinValue().toString());
			values.get(3).add(aggregateMap.get(dayTime).getMaxValue().toString());
		}

		try {
			googleSheetsIo.writeToSheet(values, TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "B" + TurnipConstants.DATA_START_ROW, false);
		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	private Integer nullMin(final Integer a, final Integer b) {
		if(a == null) {
			return b;
		}
		if(b == null) {
			return a;
		}
		return Math.min(a, b);
	}

	private Integer nullMax(final Integer a, final Integer b) {
		if(a == null) {
			return b;
		}
		if(b == null) {
			return a;
		}
		return Math.max(a, b);
	}
}
