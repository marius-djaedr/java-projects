package com.me.test.util.utils;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import com.me.util.Constants;
import com.me.util.utils.RootUtil;

public class RootUtil_Unit {

	@Test
	public void integrateTest() {
		System.out.println("\n\n===");
		doTest(x -> x - 1, 0.0, 2.0, 1.0);
		System.out.println("\n\n===");
		doTest(x -> (x - 1.) * x, -0.01, 0.1, 0.);
		System.out.println("\n\n===");
		doTest(x -> (x - 1.) * x, 0.99, 1.1, 1.);
		System.out.println("\n\n===");
		doTest(x -> (x - 1.) * x, -0.01, 1.1, 0.);

		System.out.println("\n\n===");
		doTest(Math::sin, -0.01, 0.1, 0.);
		System.out.println("\n\n===");
		doTest(Math::sin, Math.PI - .01, Math.PI + .1, Math.PI);
		System.out.println("\n\n===");
		doTest(Math::sin, -0.01, Math.PI + .1, -Math.PI);
	}

	private void doTest(final Function<Double, Double> function, final double x0, final double x1, final double expected) {
		final double actual = RootUtil.secantMethod(function, x0, x1);

		double err = Math.abs(actual - expected);
		if(Math.abs(expected) > Constants.PRECISION) {
			err = err / Math.abs(expected);
		}

		if(err > Constants.ACCURACY) {
			Assert.fail("BAD JUJU");
		}
	}
}
