package com.me.turnips.processors;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mortbay.io.RuntimeIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.io.google.GoogleSheetsIo;
import com.me.turnips.constants.CurveType;
import com.me.turnips.constants.DayTime;
import com.me.turnips.constants.TurnipConstants;
import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.DailyCost;
import com.me.turnips.dtos.UserInput;

@Component
public class BuyCurveProcessor implements IBulkCurveProcessor {
	@Autowired
	private GoogleSheetsIo googleSheetsIo;

	private Integer priceAtBought;
	private Integer priceNow;
	private Map<CurveType, BigDecimal> netProbability;
	private Map<CurveType, BigDecimal> netValue;

	@Override
	public void initialize(final UserInput userInput) {
		priceAtBought = userInput.getWeekCurve().get(DayTime.SUNDAY);
		priceNow = userInput.getWeekCurve().get(userInput.getCurrentDayTime());

		netProbability = new HashMap<>();
		netValue = new HashMap<>();
	}

	@Override
	public void process(final Collection<CostCurve> curves) {
		final Map<CurveType, List<CostCurve>> curveTypeMap = curves.stream().collect(Collectors.groupingBy(CostCurve::getCurveType));

		Arrays.asList(CurveType.values()).forEach(t -> netProbability.put(t, calculateProbability(curveTypeMap.get(t))));
		Arrays.asList(CurveType.values()).forEach(t -> netValue.put(t, calculateValue(t, curveTypeMap.get(t))));
	}

	private BigDecimal calculateProbability(final List<CostCurve> list) {
		if(list == null) {
			return BigDecimal.ZERO;
		}
		return list.stream().map(c -> c.getProbability()).reduce(BigDecimal::add).orElseGet(() -> BigDecimal.ZERO);
	}

	private BigDecimal calculateValue(final CurveType t, final List<CostCurve> list) {
		if(list == null) {
			return BigDecimal.ZERO;
		}
		if(CurveType.DECREASING == t) {
			return list.stream().map(CostCurve::getCostMap).map(m -> m.get(DayTime.THURSDAY_PM)).map(DailyCost::average).min(BigDecimal::compareTo)
					.orElseGet(() -> BigDecimal.ZERO);
		}
		return list.stream().map(CostCurve::getCostMap).map(Map::values).flatMap(Collection::stream).map(DailyCost::average)
				.max(BigDecimal::compareTo).orElseGet(() -> BigDecimal.ZERO);
	}

	@Override
	public void output() {
		try {
			googleSheetsIo.writeToSheet(Arrays.asList(Arrays.asList(Integer.toString(priceAtBought))), TurnipConstants.SHEET_ID,
					TurnipConstants.SHEET_NAME + "C2", false);
			googleSheetsIo.writeToSheet(Arrays.asList(Arrays.asList(Integer.toString(priceNow))), TurnipConstants.SHEET_ID,
					TurnipConstants.SHEET_NAME + "C6", false);

			final List<String> small = curveTypeRow(CurveType.SMALL_SPIKE);
			final List<String> large = curveTypeRow(CurveType.LARGE_SPIKE);
			final List<String> decrease = curveTypeRow(CurveType.DECREASING);
			final List<String> fluctuating = curveTypeRow(CurveType.FLUCTUATING);
			final List<List<String>> values = Arrays.asList(small, large, decrease, fluctuating);

			googleSheetsIo.writeToSheet(values, TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "B8", false);

		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	private List<String> curveTypeRow(final CurveType curveType) {
		return Arrays.asList(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(netProbability.get(curveType)),
				TurnipConstants.BIG_DECIMAL_FORMATTER.apply(netValue.get(curveType)));
	}

}
