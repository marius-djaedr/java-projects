package com.me.util.dto;

import com.me.util.Constants;

public class MaxwellDistribution {
	private final double expMult, leftBound, upperBound;

	public MaxwellDistribution(final double leftBound, final double peak) {
		this.leftBound = leftBound;

		if(peak < leftBound) {
			throw new IllegalArgumentException("peak must be greater than lower bound!");
		}
		this.expMult = -1. / ((peak - leftBound) * (peak - leftBound));

		this.upperBound = computeUpperBound();
	}

	public double unshiftedUnscaledFunction(final double x) {
		return x * x * Math.exp(expMult * x * x);
	}

	public double shiftedUnscaledFunction(final double x) {
		return unshiftedUnscaledFunction(x - leftBound);
	}

	public double lowerBound() {
		return leftBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	private double computeUpperBound() {
		double xn = 1;
		while(true) {
			xn = xn * 2.;
			if(unshiftedUnscaledFunction(xn) < Constants.PRECISION) {
				return xn + leftBound;
			}
		}
	}
}
