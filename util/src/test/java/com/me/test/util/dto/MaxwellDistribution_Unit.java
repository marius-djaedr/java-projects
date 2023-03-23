package com.me.test.util.dto;

import java.util.function.Function;

import org.junit.Test;

import com.me.util.dto.MaxwellDistribution;
import com.me.util.utils.StatUtil;

public class MaxwellDistribution_Unit {

	@Test
	public void test() {
		final MaxwellDistribution func = new MaxwellDistribution(1., 5.7);

		final Function<Double, Double> pdf = StatUtil.computePdf(func::shiftedUnscaledFunction, func.lowerBound(), func.getUpperBound());
		final Function<Double, Double> cdf = StatUtil.computeCdf(pdf, func.lowerBound());

		for(double x = func.lowerBound() ; x < func.getUpperBound() ; x += .1) {
			System.out.println(String.format("  %13.10f   :  %13.10f   :  %13.10f   :  %13.10f", x, func.shiftedUnscaledFunction(x), pdf.apply(x),
					cdf.apply(x)));
		}
	}
}
