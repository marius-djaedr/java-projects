package com.me.turnips;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import org.mortbay.io.RuntimeIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.io.google.GoogleSheetsIo;
import com.me.turnips.constants.DayTime;
import com.me.turnips.constants.TurnipConstants;
import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.DailyCost;
import com.me.turnips.dtos.UserInput;
import com.me.turnips.processors.IBulkCurveProcessor;
import com.me.turnips.processors.ICurveProcessor;
import com.me.turnips.processors.IEachCurveProcessor;
import com.me.turnips.processors.IEachElementCurveProcessor;
import com.me.turnips.services.ICurveInputService;
import com.me.turnips.services.IUserInputService;

@Component
public class LaunchWrapper {
	@Autowired
	private GoogleSheetsIo googleSheetsIo;

	@Autowired
	private IUserInputService userInputService;
	@Autowired
	private ICurveInputService curveInputService;
	@Autowired
	private ApplicationContext context;

	public void launch() {
		final UserInput userInput = userInputService.getUserInput();
		final List<CostCurve> curves = curveInputService.getCurves(userInput);
		outputCurves(curves);
		final Collection<ICurveProcessor> processors = context.getBeansOfType(ICurveProcessor.class).values();
		processors.forEach(p -> p.initialize(userInput));

		//handle bulk processors
		processors.stream().filter(p -> IBulkCurveProcessor.class.isAssignableFrom(p.getClass()))
				.forEach(p -> ((IBulkCurveProcessor) p).process(curves));

		//handle curve-wise and element-wise processors
		final List<IEachCurveProcessor> eachCurveProcessors = processors.stream()
				.filter(p -> IEachCurveProcessor.class.isAssignableFrom(p.getClass())).map(IEachCurveProcessor.class::cast)
				.collect(Collectors.toList());
		final List<IEachElementCurveProcessor> eachElementProcessors = processors.stream()
				.filter(p -> IEachElementCurveProcessor.class.isAssignableFrom(p.getClass())).map(IEachElementCurveProcessor.class::cast)
				.collect(Collectors.toList());

		for(final CostCurve curve : curves) {
			eachCurveProcessors.forEach(p -> p.process(curve));
			final EnumMap<DayTime, DailyCost> costMap = curve.getCostMap();
			for(final DayTime dayTime : DayTime.values()) {
				eachElementProcessors.forEach(p -> p.process(curve, dayTime, costMap.get(dayTime)));
			}
		}
		processors.forEach(ICurveProcessor::output);
	}

	//TODO extract this and all other output into some kind of service layer independent of output
	private void outputCurves(final List<CostCurve> curves) {
		final List<List<String>> values = new ArrayList<>();
		final List<String> header = new ArrayList<>();
		header.add("Type");
		header.add("Percent");
		header.add("Type");
		header.addAll(TurnipConstants.DAY_TIME_HEADERS);
		values.add(header);
		for(final CostCurve curve : curves) {
			final List<String> row = new ArrayList<>();
			row.add(curve.getCurveType().toString());
			row.add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(curve.getProbability()));
			row.add(curve.getCurveType().toString());
			for(final DayTime dayTime : DayTime.values()) {
				row.add(TurnipConstants.BIG_DECIMAL_FORMATTER.apply(curve.getCostMap().get(dayTime).average()));
			}
			values.add(row);
		}
		for(int i = values.size() ; i <= 270 ; i++) {
			values.add(Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
		}

		try {
			googleSheetsIo.writeToSheet(values, TurnipConstants.SHEET_ID, TurnipConstants.SHEET_NAME + "O38", false);
		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}

	}

}
