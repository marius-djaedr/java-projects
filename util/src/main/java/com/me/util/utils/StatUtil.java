package com.me.util.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import com.me.util.Constants;

public class StatUtil {

	private static final int SCALE = 10;

	public static BigDecimal sqrt(final BigDecimal A, final int scale) {
		BigDecimal x0 = BigDecimal.ZERO;
		BigDecimal x1 = BigDecimal.valueOf(Math.sqrt(A.doubleValue()));
		while(x0.compareTo(x1) != 0) {
			x0 = x1;
			x1 = A.divide(x0, scale, RoundingMode.HALF_UP);
			x1 = x1.add(x0);
			x1 = x1.divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);
		}
		return x1;
	}

	public static <T> TreeMap<T, Double> computeDiscretePdf(final Map<T, Double> weightMap, final Comparator<T> comparator) {

		double sum = 0.;
		final TreeMap<T, Double> pdf = new TreeMap<>();
		final List<T> sortedKeys = new ArrayList<>(weightMap.keySet());
		sortedKeys.sort(comparator);

		for(final T key : sortedKeys) {
			final double weight = weightMap.get(key);
			if(weight < Constants.PRECISION) {
				throw new IllegalArgumentException("Invalid weight [" + weight + "] for key [" + key + "]. Must be a positive number");
			}
			sum = sum + weight;
			pdf.put(key, weight);
		}

		for(final T key : sortedKeys) {
			pdf.put(key, pdf.get(key) / sum);
		}

		return pdf;
	}

	/**
	 * 
	 * @param <T>
	 * @param discretePdf must already be normalized. see {@link #computeDiscretePdf(Map, Comparator)}
	 * @return
	 */
	public static <T> TreeMap<T, Double> computeDiscreteCdf(final TreeMap<T, Double> discretePdf) {

		double cumulative = 0.;
		final TreeMap<T, Double> discreteCdf = new TreeMap<>();
		for(final T key : discretePdf.keySet()) {
			cumulative = cumulative + discretePdf.get(key);
			discreteCdf.put(key, cumulative);
		}

		return discreteCdf;
	}

	/**
	 * converts any function into a PDF. First, takes absolute value to make everything non-negative, then normalizes across the interval
	 * 
	 * @param func
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static Function<Double, Double> computePdf(final Function<Double, Double> func, final double lowerBound, final double upperBound) {
		final double r = CalculusUtil.integrate(x -> Math.abs(func.apply(x)), lowerBound, upperBound);
		return x -> Math.abs(func.apply(x)) / r;
	}

	/**
	 * 
	 * @param pdf must already be normalized. see {@link #computePdf(Function, double, double)}
	 * @param lowerBound
	 * @return
	 */
	public static Function<Double, Double> computeCdf(final Function<Double, Double> pdf, final double lowerBound) {
		return x -> CalculusUtil.integrate(pdf, lowerBound, x);
	}

}
