package com.me.util.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class RollUtil {
	private static final Random RAND = new Random();

	public static int roll(final int upper) {
		return RAND.nextInt(upper);
	}

	public static int roll(final int lower, final int upper) {
		return roll(upper - lower) + lower;
	}

	public static int numToGenerate() {
		final double num = Math.floor(1.0 - Math.log10(RAND.nextDouble()));
		return (int) num;
	}

	@SafeVarargs
	public static <T> T randomElement(final T... list) {
		return randomElement(Arrays.asList(list));
	}

	public static <T> T randomElement(final Collection<T> collection) {
		final List<T> list = new ArrayList<>(collection);
		if(list == null || list.size() == 0) {
			return null;
		}
		return list.get(roll(list.size()));
	}

	public static <T> T randomElement(final Map<T, Double> weightMap) {
		final double totalWeight = weightMap.values().stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
		if(totalWeight == 0.) {
			return null;
		}

		final double rd = RAND.nextDouble();
		double sum = 0.;
		for(final Map.Entry<T, Double> entry : weightMap.entrySet()) {
			if(entry.getValue() == null) {
				continue;
			}
			sum += entry.getValue().doubleValue() / totalWeight;
			if(sum > rd) {
				return entry.getKey();
			}
		}
		throw new UnsupportedOperationException("No idea how we got here, shouldn't be possible");
	}

	public static double randomFromCdf(final Function<Double, Double> cdf, final double lowerBound, final double upperBound) {
		final double rd = RAND.nextDouble();
		final Function<Double, Double> rootFunc = x -> rd - cdf.apply(x);

		final double diff = rd * (upperBound - lowerBound);

		return RootUtil.secantMethod(rootFunc, lowerBound + diff, upperBound - diff);
	}

	public static String existingOrNew() {
		return randomElement(Arrays.asList("existing", "new"));
	}
}
