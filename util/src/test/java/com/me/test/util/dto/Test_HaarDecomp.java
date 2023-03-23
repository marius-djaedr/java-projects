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

import com.me.util.dto.HaarDecomp;

public class Test_HaarDecomp {
	private final BinaryOperator<Double> addFunc = (i1, i2) -> i1 + i2;
	private final UnaryOperator<Double> negativeFunc = i -> -i;
	private final UnaryOperator<Double> divByTwoFunc = i -> i / 2.;

	@Test(expected = IllegalArgumentException.class)
	public void test_mismatchedSizeException() {
		new HaarDecomp<>(Arrays.asList(1.), Arrays.asList(1., 2.), addFunc, negativeFunc, divByTwoFunc);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_sizeNotPow2Exception() {
		new HaarDecomp<>(Arrays.asList(1., 2., 3.), Arrays.asList(1., 2., 3.), addFunc, negativeFunc, divByTwoFunc);
	}

	@Test
	public void test_valid4() {
		testHaar(Arrays.asList(1., 2., 3., 4.), 2, Arrays.asList(Arrays.asList(2.5), Arrays.asList(1.5, 3.5), Arrays.asList(1., 2., 3., 4.)));
	}

	private void testHaar(final List<Double> values, final int expectedL, final List<List<Double>> expectedDecomp) {
		final List<Integer> keys = IntStream.range(0, values.size()).boxed().collect(Collectors.toList());
		final HaarDecomp<Integer, Double> hd = new HaarDecomp<>(keys, values, addFunc, negativeFunc, divByTwoFunc);

		Assert.assertEquals("L", expectedL, hd.getL());

		//make sure VL is the original values
		final List<Pair<Integer, Double>> plotL = hd.plotVL();
		for(int i = 0 ; i < values.size() ; i++) {
			Assert.assertEquals("XL i=" + i, (Integer) i, plotL.get(i).getLeft());
			Assert.assertEquals("VL i=" + i, values.get(i), plotL.get(i).getRight());
		}

		//check each Vl
		for(int l = 0 ; l <= expectedL ; l++) {
			final List<Pair<Integer, Double>> plotl = hd.plotVl(l);
			for(int i = 0 ; i < plotl.size() ; i++) {
				Assert.assertEquals("V" + l + " i=" + i, expectedDecomp.get(l).get(i), plotl.get(i).getRight());
			}
		}
	}
}
