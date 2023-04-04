package com.me.turnips.services;

import java.util.EnumMap;

import org.springframework.stereotype.Service;

import com.me.turnips.constants.CurveType;
import com.me.turnips.constants.DayTime;
import com.me.turnips.dtos.UserInput;

@Service
public class HardcodedUserInputService implements IUserInputService {

	//TODO a better way to pass these in
	private static final boolean firstTime = false;
	private static final CurveType previousPattern = CurveType.DECREASING;
	private static final EnumMap<DayTime, Integer> weekCurve = buildWeekCurve();

	private static final DayTime currentDayTime = DayTime.TUESDAY_AM;

	private static EnumMap<DayTime, Integer> buildWeekCurve() {
		final EnumMap<DayTime, Integer> input = new EnumMap<>(DayTime.class);
		input.put(DayTime.SUNDAY, 98);
		input.put(DayTime.MONDAY_AM, 84);
		input.put(DayTime.MONDAY_PM, 79);
		input.put(DayTime.TUESDAY_AM, 75);
		input.put(DayTime.TUESDAY_PM, null);
		input.put(DayTime.WEDNESDAY_AM, null);
		input.put(DayTime.WEDNESDAY_PM, null);
		input.put(DayTime.THURSDAY_AM, null);
		input.put(DayTime.THURSDAY_PM, null);
		input.put(DayTime.FRIDAY_AM, null);
		input.put(DayTime.FRIDAY_PM, null);
		input.put(DayTime.SATURDAY_AM, null);
		input.put(DayTime.SATURDAY_PM, null);
		return input;
	}

	@Override
	public UserInput getUserInput() {
		return new UserInput(firstTime, previousPattern, weekCurve, currentDayTime);
	}

}
