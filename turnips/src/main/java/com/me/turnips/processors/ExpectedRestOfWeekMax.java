package com.me.turnips.processors;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
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
public class ExpectedRestOfWeekMax implements IEachElementCurveProcessor {
	private Map<CostCurve, BigDecimal> curveMax;
	@Autowired
	private GoogleSheetsIo googleSheetsIo;

	private DayTime currentDayTime;

	@Override
	public void initialize(final UserInput userInput) {
		currentDayTime = userInput.getCurrentDayTime();
		curveMax = new HashMap<>();
	}

	@Override
	public void process(final CostCurve curve, final DayTime dayTime, final DailyCost curveCost) {
		if(dayTime.ordinal() >= currentDayTime.ordinal()) {
			BigDecimal currentMax = curveMax.getOrDefault(curve, BigDecimal.ZERO);
			final BigDecimal average = curveCost.average();
			if(average != null) {
				currentMax = currentMax.max(average);
			}
			curveMax.put(curve, currentMax);
		}
	}

	@Override
	public void output() {
		BigDecimal expectedMax = BigDecimal.ZERO;
		for(final CostCurve curve : curveMax.keySet()) {
			final BigDecimal prob = curve.getProbability();
			expectedMax = expectedMax.add(curveMax.get(curve).multiply(prob));
		}

		try {
			googleSheetsIo.writeToSheet(Arrays.asList(Arrays.asList(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(expectedMax))),
					TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "E18", false);

		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}

	}

}
