package com.me.util.utils;

import java.util.function.Function;

import com.me.util.Constants;

public class CalculusUtil {

	/**
	 * uses basic trapezoid rule
	 * @return
	 */
	public static double integrate(final Function<Double, Double> integrand, final double lowerBound, final double upperBound) {

		double N = 1.;
		double result = 0;
		double err = Double.MAX_VALUE;

		while(err > Constants.ACCURACY) {
			final double prior = result;
			N = N * 2;
			result = integrateStepN(N, integrand, lowerBound, upperBound);
			err = Math.abs(prior - result);
			if(Math.abs(result) > Constants.PRECISION) {
				err = err / Math.abs(result);
			}
			//			System.out.println("N [" + N + "], result [" + result + "], err[" + err + "]");
		}

		return result;
	}

	private static double integrateStepN(final double N, final Function<Double, Double> integrand, final double lowerBound, final double upperBound) {
		final double delX = (upperBound - lowerBound) / N;

		double sum = (integrand.apply(lowerBound) + integrand.apply(upperBound)) / 2.;

		for(double n = 1.0 ; n < N ; n++) {
			final double x = lowerBound + delX * n;
			sum = sum + integrand.apply(x);
		}

		return sum * delX;
	}
}
