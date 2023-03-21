package com.me.turnips.dtos;

import java.util.EnumMap;

import com.me.turnips.constants.CurveType;
import com.me.turnips.constants.DayTime;

public class UserInput {

	boolean firstTime;
	CurveType previousPattern;
	EnumMap<DayTime, Integer> weekCurve;
	DayTime currentDayTime;

	public UserInput(final boolean firstTime, final CurveType previousPattern, final EnumMap<DayTime, Integer> weekCurve,
			final DayTime currentDayTime) {
		this.firstTime = firstTime;
		this.previousPattern = previousPattern;
		this.weekCurve = weekCurve;
		this.currentDayTime = currentDayTime;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(final boolean firstTime) {
		this.firstTime = firstTime;
	}

	public CurveType getPreviousPattern() {
		return previousPattern;
	}

	public void setPreviousPattern(final CurveType previousPattern) {
		this.previousPattern = previousPattern;
	}

	public EnumMap<DayTime, Integer> getWeekCurve() {
		return weekCurve;
	}

	public void setWeekCurve(final EnumMap<DayTime, Integer> weekCurve) {
		this.weekCurve = weekCurve;
	}

	public DayTime getCurrentDayTime() {
		return currentDayTime;
	}

	public void setCurrentDayTime(final DayTime currentDayTime) {
		this.currentDayTime = currentDayTime;
	}

}
