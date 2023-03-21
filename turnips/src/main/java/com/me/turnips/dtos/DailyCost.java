package com.me.turnips.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public class DailyCost implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer minValue;
	private Integer maxValue;

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(final Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(final Integer maxValue) {
		this.maxValue = maxValue;
	}

	public BigDecimal average() {
		if(minValue == null && maxValue == null) {
			return null;
		}
		return BigDecimal.valueOf(minValue + maxValue).setScale(2).divide(BigDecimal.valueOf(2).setScale(2));
	}

	public static DailyCost forAgg() {
		final DailyCost cost = new DailyCost();
		cost.minValue = Integer.MAX_VALUE;
		cost.maxValue = Integer.MIN_VALUE;
		return cost;
	}

	public static DailyCost singleVal(final Integer val) {
		final DailyCost cost = new DailyCost();
		cost.minValue = val;
		cost.maxValue = val;
		return cost;
	}

	@Override
	public String toString() {
		return "min=" + minValue + ",max=" + maxValue;
	}
}
