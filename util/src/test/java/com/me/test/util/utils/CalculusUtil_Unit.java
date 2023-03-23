package com.me.test.util.utils;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import com.me.util.Constants;
import com.me.util.utils.CalculusUtil;

public class CalculusUtil_Unit {

	@Test
	public void integrateTest() {
		doTest(Function.identity(), 0.0, 1.0, 0.5);
		doTest(Function.identity(), 10.0, 15.0, 62.5);
		doTest(x -> x * x, 0.0, 1.0, 1. / 3.);
		doTest(x -> x * x, 10.0, 15.0, 2375. / 3.);

		doTest(Math::sin, 0.0, Math.PI, 2.0);
	}

	private void doTest(final Function<Double, Double> integrand, final double lowerBound, final double upperBound, final double expected) {
		final double actual = CalculusUtil.integrate(integrand, lowerBound, upperBound);

		double err = Math.abs(actual - expected);
		if(Math.abs(expected) > Constants.PRECISION) {
			err = err / Math.abs(expected);
		}

		if(err > Constants.ACCURACY) {
			Assert.fail("BAD JUJU");
		}
	}
}
