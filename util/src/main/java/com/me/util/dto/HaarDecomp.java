package com.me.util.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.tuple.Pair;

public class HaarDecomp<K, T> {
	private final BinaryOperator<T> addFunc;
	private final UnaryOperator<T> negativeFunc;
	private final int L;
	private final List<List<K>> X;
	private final List<List<T>> V;
	private final List<List<T>> W;
	private List<List<T>> Vp;
	private List<List<T>> Wp;

	public HaarDecomp(final List<K> XL, final List<T> VL, final BinaryOperator<T> addFunc, final UnaryOperator<T> negativeFunc,
			final UnaryOperator<T> divByTwoFunc) {
		if(XL.size() != VL.size()) {
			throw new IllegalArgumentException("Key and Value maps must be equal size! Had [" + XL.size() + "] and [" + VL.size() + "]");
		}
		final int x2floor = (int) Math.floor(Math.log(XL.size()) / Math.log(2.));
		final int x2ceil = (int) Math.ceil(Math.log(XL.size()) / Math.log(2.));

		if(x2floor != x2ceil) {
			throw new IllegalArgumentException("Size must be power of 2! (2,4,8,16,32,...), was [" + XL.size() + "]");
		}

		this.addFunc = addFunc;
		this.negativeFunc = negativeFunc;
		this.L = x2floor;

		X = new ArrayList<>();
		V = new ArrayList<>();
		W = new ArrayList<>();
		X.add(0, XL);
		V.add(0, VL);
		W.add(0, null);

		List<K> Xl = XL;
		List<T> Vl = VL;
		for(int l = L - 1 ; l >= 0 ; l--) {
			final int Jm = Vl.size() / 2;
			final List<K> Xlm = new ArrayList<>();
			final List<T> Vlm = new ArrayList<>();
			final List<T> Wlm = new ArrayList<>();
			for(int jm = 0 ; jm < Jm ; jm++) {
				final int jm2 = jm * 2;

				Xlm.add(Xl.get(jm2));
				Vlm.add(jm, divByTwoFunc.apply(addFunc.apply(Vl.get(jm2), Vl.get(jm2 + 1))));
				Wlm.add(jm, divByTwoFunc.apply(addFunc.apply(Vl.get(jm2), negativeFunc.apply(Vl.get(jm2 + 1)))));
			}
			X.add(0, Xlm);
			V.add(0, Vlm);
			W.add(0, Wlm);
			Xl = Xlm;
			Vl = Vlm;
		}
		Vp = V;
		Wp = W;
	}

	public HaarDecomp<K, T> process(final UnaryOperator<List<List<T>>> processFunc) {
		Wp = processFunc.apply(W);
		reconstruct();
		return this;
	}

	private void reconstruct() {
		Vp = new ArrayList<>();
		Vp.add(V.get(0));
		for(int l = 1 ; l <= L ; l++) {
			final List<T> Vpl = new ArrayList<>();
			final List<T> Vplm = Vp.get(l - 1);
			final List<T> Wplm = Wp.get(l - 1);

			for(int jm = 0 ; jm < Vplm.size() ; jm++) {
				final T vplmjm = Vplm.get(jm);
				final T wplmjm = Wplm.get(jm);
				Vpl.add(addFunc.apply(vplmjm, wplmjm));
				Vpl.add(addFunc.apply(vplmjm, negativeFunc.apply(wplmjm)));
			}
			Vp.add(Vpl);
		}
	}

	public int getL() {
		return L;
	}

	public List<Pair<K, T>> plotVL() {
		return plotVl(L);
	}

	public List<Pair<K, T>> plotVl(final int l) {
		return internalPlot(l, V);
	}

	public List<Pair<K, T>> plotVpL() {
		return plotVpl(L);
	}

	public List<Pair<K, T>> plotVpl(final int l) {
		return internalPlot(l, Vp);
	}

	public List<Pair<K, T>> plotWl(final int l) {
		return internalPlot(l, W);
	}

	public List<Pair<K, T>> plotWpl(final int l) {
		return internalPlot(l, Wp);
	}

	private List<Pair<K, T>> internalPlot(final int l, final List<List<T>> T) {
		final List<K> Xl = X.get(l);
		final List<T> Tl = T.get(l);

		final List<Pair<K, T>> ret = new ArrayList<>();
		for(int j = 0 ; j < Xl.size() ; j++) {
			ret.add(Pair.of(Xl.get(j), Tl.get(j)));
		}

		return ret;
	}
}
