package com.me.util.quickmains;

public class SeeSaySequence {

	public static final String SEED = "1";

	public static void main(final String[] args) {
		String prior = SEED;
		System.out.println(String.format(" %10d : ", prior.length()) + prior);
		for(int i = 0 ; i < 40 ; i++) {
			prior = calculateNext(prior);
			System.out.println(String.format(" %10d : ", prior.length()) + prior);
		}
	}

	private static String calculateNext(final String prior) {
		int count = 1;
		char current = prior.charAt(0);
		final StringBuilder next = new StringBuilder("");

		for(int i = 1 ; i < prior.length() ; i++) {
			final char charI = prior.charAt(i);
			if(charI == current) {
				count++;
			} else {
				next.append(count).append(current);
				count = 1;
				current = charI;
			}
		}
		next.append(count).append(current);
		return next.toString();
	}
}
