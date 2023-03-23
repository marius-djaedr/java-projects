package com.me.util.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.tuple.Pair;

public class RightPartialHaarDecomp<K, T> {
	private final List<HaarDecomp<K, T>> cells;
	private final int maxL;

	public RightPartialHaarDecomp(final List<K> XL, final List<T> VL, final BinaryOperator<T> addFunc, final UnaryOperator<T> negativeFunc,
			final UnaryOperator<T> divByTwoFunc) {
		if(XL.size() != VL.size()) {
			throw new IllegalArgumentException("Key and Value maps must be equal size! Had [" + XL.size() + "] and [" + VL.size() + "]");
		}
		cells = new ArrayList<>();

		List<K> mK = new ArrayList<>(XL);
		List<T> mV = new ArrayList<>(VL);

		while(mK.size() > 0) {
			final int pow2Below = (int) Math.floor(Math.log(mK.size()) / Math.log(2.));
			final int n2 = (int) Math.pow(2., pow2Below);
			final int fromInd = mK.size() - n2;
			final int toInd = mK.size();

			cells.add(0, new HaarDecomp<>(new ArrayList<>(mK.subList(fromInd, toInd)), new ArrayList<>(mV.subList(fromInd, toInd)), addFunc,
					negativeFunc, divByTwoFunc));

			mK = new ArrayList<>(mK.subList(0, fromInd));
			mV = new ArrayList<>(mV.subList(0, fromInd));
		}

		maxL = cells.stream().map(HaarDecomp::getL).max(Integer::compareTo).get();
	}

	public int getMaxL() {
		return maxL;
	}

	public List<Pair<K, T>> plotVL() {
		final List<Pair<K, T>> plot = new ArrayList<>();
		cells.stream().map(HaarDecomp::plotVL).forEachOrdered(plot::addAll);
		return plot;
	}

	public List<Pair<K, T>> plotVl(final int l) {
		final List<Pair<K, T>> plot = new ArrayList<>();
		final int maxL_min_l = maxL - l;
		for(final HaarDecomp<K, T> cell : cells) {
			final int lCell = Math.max(0, cell.getL() - maxL_min_l);
			plot.addAll(cell.plotVl(lCell));
		}
		return plot;
	}

	public RightPartialHaarDecomp<K, T> process(final UnaryOperator<List<List<T>>> processFunc) {
		cells.forEach(h -> h.process(processFunc));
		return this;
	}
}
