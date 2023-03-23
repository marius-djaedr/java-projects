package com.me.util.utils;

import java.util.function.Function;

import com.me.util.Constants;

public class RootUtil {

	public static double secantMethod(final Function<Double, Double> function, final double x0, final double x1) {
		if(Math.abs(x1 - x0) < Constants.PRECISION) {
			//TODO something here
			throw new IllegalArgumentException("damnit..");
		}
		double xn = x1;
		double fn = function.apply(xn);
		double xnm1 = x0;
		double fnm1 = function.apply(xnm1);

		while(Math.abs(fn) > Constants.ACCURACY) {
			final double fnm2 = fnm1;
			final double xnm2 = xnm1;
			fnm1 = fn;
			xnm1 = xn;
			if(Math.abs(fnm1 - fnm2) < Constants.PRECISION) {
				//TODO something here
				throw new IllegalArgumentException("damnit 2..");
			}
			xn = (xnm2 * fnm1 - xnm1 * fnm2) / (fnm1 - fnm2);
			fn = function.apply(xn);
			System.out
					.println("xn [" + xn + "], fn [" + fn + "], xn-1 [" + xnm1 + "], fn-1 [" + fnm1 + "], xn-2 [" + xnm2 + "], fn-2 [" + fnm2 + "]");
		}
		return xn;
	}
}
