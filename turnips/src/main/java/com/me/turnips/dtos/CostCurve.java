package com.me.turnips.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.EnumMap;

import com.me.turnips.constants.CurveType;
import com.me.turnips.constants.DayTime;

public class CostCurve implements Serializable {
	private static final long serialVersionUID = 1L;
	private CurveType curveType;
	private BigDecimal probability;
	private EnumMap<DayTime, DailyCost> costMap;

	public CurveType getCurveType() {
		return curveType;
	}

	public void setCurveType(final CurveType curveType) {
		this.curveType = curveType;
	}

	public BigDecimal getProbability() {
		return probability;
	}

	public void setProbability(final BigDecimal probability) {
		this.probability = probability;
	}

	public EnumMap<DayTime, DailyCost> getCostMap() {
		return costMap;
	}

	public void setCostMap(final EnumMap<DayTime, DailyCost> costMap) {
		this.costMap = costMap;
	}
}
