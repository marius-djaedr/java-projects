package com.me.test.util.dto;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import com.me.util.dto.RightPartialHaarDecomp;

public class Test_RightPartialHaarDecomp {
	private final BinaryOperator<Double> addFunc = (i1, i2) -> i1 + i2;
	private final UnaryOperator<Double> negativeFunc = i -> -i;
	private final UnaryOperator<Double> divByTwoFunc = i -> i / 2.;

	@Test(expected = IllegalArgumentException.class)
	public void test_mismatchedSizeException() {
		new RightPartialHaarDecomp<>(Arrays.asList(1.), Arrays.asList(1., 2.), addFunc, negativeFunc, divByTwoFunc);
	}

	@Test
	public void test_valid4() {
		testRPH(Arrays.asList(1., 2., 3., 4.), 2, Arrays.asList(Arrays.asList(Pair.of(0, 2.5)), Arrays.asList(Pair.of(0, 1.5), Pair.of(2, 3.5)),
				Arrays.asList(Pair.of(0, 1.), Pair.of(1, 2.), Pair.of(2, 3.), Pair.of(3, 4.))));
	}

	@Test
	public void test_valid6() {
		testRPH(Arrays.asList(1., 2., 3., 4., 5., 6.), 2,
				Arrays.asList(Arrays.asList(Pair.of(0, 1.5), Pair.of(2, 4.5)), Arrays.asList(Pair.of(0, 1.5), Pair.of(2, 3.5), Pair.of(4, 5.5)),
						Arrays.asList(Pair.of(0, 1.), Pair.of(1, 2.), Pair.of(2, 3.), Pair.of(3, 4.), Pair.of(4, 5.), Pair.of(5, 6.))));
	}

	private void testRPH(final List<Double> values, final int expectedMaxL, final List<List<Pair<Integer, Double>>> expectedDecomp) {
		final List<Integer> keys = IntStream.range(0, values.size()).boxed().collect(Collectors.toList());
		final RightPartialHaarDecomp<Integer, Double> hd = new RightPartialHaarDecomp<>(keys, values, addFunc, negativeFunc, divByTwoFunc);

		Assert.assertEquals("L", expectedMaxL, hd.getMaxL());

		//make sure VL is the original values
		final List<Pair<Integer, Double>> plotL = hd.plotVL();
		for(int i = 0 ; i < values.size() ; i++) {
			Assert.assertEquals("XL i=" + i, (Integer) i, plotL.get(i).getLeft());
			Assert.assertEquals("VL i=" + i, values.get(i), plotL.get(i).getRight());
		}

		//check each Vl
		for(int l = 0 ; l <= expectedMaxL ; l++) {
			final List<Pair<Integer, Double>> plotl = hd.plotVl(l);
			for(int i = 0 ; i < plotl.size() ; i++) {
				Assert.assertEquals("X" + l + " i=" + i, expectedDecomp.get(l).get(i).getLeft(), plotl.get(i).getLeft());
				Assert.assertEquals("V" + l + " i=" + i, expectedDecomp.get(l).get(i).getRight(), plotl.get(i).getRight());
			}
		}
	}
}
