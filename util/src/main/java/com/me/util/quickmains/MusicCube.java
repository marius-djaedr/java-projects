package com.me.util.quickmains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MusicCube {
	private static final int[][] SUM_INDICES = new int[][]{{0, 1, 10, 11}, {0, 2, 3, 5}, {1, 2, 4, 6}, {3, 4, 7, 8}, {5, 7, 9, 10}, {6, 8, 9, 11}};

	@SuppressWarnings("unchecked")
	public static void main(final String[] args) {
		final List<int[]> configs = new ArrayList<>();

		final HashSet<Integer> aNums = (HashSet<Integer>) IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toSet());
		a: for(final int a : aNums) {
			final HashSet<Integer> bNums = (HashSet<Integer>) aNums.clone();
			bNums.remove(a);
			b: for(final int b : bNums) {
				final HashSet<Integer> kNums = (HashSet<Integer>) bNums.clone();
				kNums.remove(b);
				k: for(final int k : kNums) {
					//calculate optimum l
					final int l = 26 - a - b - k;
					if(l < 1 || l > 12 || l == a || l == b || l == k) {
						continue k;
					}
					final HashSet<Integer> cNums = (HashSet<Integer>) kNums.clone();
					cNums.remove(k);
					cNums.remove(l);
					c: for(final int c : cNums) {
						final HashSet<Integer> dNums = (HashSet<Integer>) cNums.clone();
						dNums.remove(c);
						d: for(final int d : dNums) {
							//calculate optimum f
							final int f = 26 - a - c - d;
							if(f < 1 || f > 12 || f == a || f == b || f == c || f == d || f == k || f == l) {
								continue d;
							}

							final HashSet<Integer> eNums = (HashSet<Integer>) dNums.clone();
							eNums.remove(d);
							eNums.remove(f);
							e: for(final int e : eNums) {
								//calculate optimum g
								final int g = 26 - b - c - e;
								if(g < 1 || g > 12 || g == a || g == b || g == c || g == d || g == e || g == f || g == k || g == l) {
									continue e;
								}

								final HashSet<Integer> hNums = (HashSet<Integer>) eNums.clone();
								hNums.remove(e);
								hNums.remove(g);
								h: for(final int h : hNums) {
									//calculate optimum i
									final int i = 26 - d - e - h;
									if(i < 1 || i > 12 || i == a || i == b || i == c || i == d || i == e || i == f || i == g || i == h || i == k
											|| i == l) {
										continue h;
									}

									final HashSet<Integer> jNums = (HashSet<Integer>) hNums.clone();
									jNums.remove(h);
									jNums.remove(i);
									j: for(final int j : jNums) {
										configs.add(new int[]{a, b, c, d, e, f, g, h, i, j, k, l});
									}
								}
							}
						}
					}
				}
			}
		}

		final Set<List<Integer>> unique = new HashSet<>();
		final Set<List<Integer>> duplicates = new HashSet<>();
		for(final int[] config : configs) {
			final List<Integer> configList = IntStream.of(config).boxed().collect(Collectors.toList());
			if(duplicates.contains(configList)) {
				continue;
			}
			unique.add(configList);
			addDuplicates(configList, duplicates);
		}

		System.out.println("UNIQUE");
		unique.forEach(MusicCube::outputResult);
	}

	private static void addDuplicates(List<Integer> config, final Set<List<Integer>> duplicates) {
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);

		config = transpose(config);
		addFlipX(config, duplicates);
	}

	private static void addFlipX(List<Integer> config, final Set<List<Integer>> duplicates) {
		addFlipY(config, duplicates);

		config = flipx(config);
		addFlipY(config, duplicates);
	}

	private static void addFlipY(List<Integer> config, final Set<List<Integer>> duplicates) {
		addFlipZ(config, duplicates);

		config = flipy(config);
		addFlipZ(config, duplicates);
	}

	private static void addFlipZ(List<Integer> config, final Set<List<Integer>> duplicates) {
		addRotationsX(config, duplicates);

		config = flipz(config);
		addRotationsX(config, duplicates);
	}

	private static void addRotationsX(List<Integer> config, final Set<List<Integer>> duplicates) {
		addRotationsY(config, duplicates);

		config = rotatex(config);
		addRotationsY(config, duplicates);

		config = rotatex(config);
		addRotationsY(config, duplicates);

		config = rotatex(config);
		addRotationsY(config, duplicates);
	}

	private static void addRotationsY(List<Integer> config, final Set<List<Integer>> duplicates) {
		addRotationsZ(config, duplicates);

		config = rotatey(config);
		addRotationsZ(config, duplicates);

		config = rotatey(config);
		addRotationsZ(config, duplicates);

		config = rotatey(config);
		addRotationsZ(config, duplicates);
	}

	private static void addRotationsZ(List<Integer> config, final Set<List<Integer>> duplicates) {
		duplicates.add(config);

		config = rotatez(config);
		duplicates.add(config);

		config = rotatez(config);
		duplicates.add(config);

		config = rotatez(config);
		duplicates.add(config);
	}

	private static List<Integer> transpose(final List<Integer> config) {
		final int a = config.get(0) % 12 + 1;
		final int b = config.get(1) % 12 + 1;
		final int c = config.get(2) % 12 + 1;
		final int d = config.get(3) % 12 + 1;
		final int e = config.get(4) % 12 + 1;
		final int f = config.get(5) % 12 + 1;
		final int g = config.get(6) % 12 + 1;
		final int h = config.get(7) % 12 + 1;
		final int i = config.get(8) % 12 + 1;
		final int j = config.get(9) % 12 + 1;
		final int k = config.get(10) % 12 + 1;
		final int l = config.get(11) % 12 + 1;
		return Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	private static List<Integer> rotatex(final List<Integer> config) {
		final int a = config.get(0);
		final int b = config.get(1);
		final int c = config.get(2);
		final int d = config.get(3);
		final int e = config.get(4);
		final int f = config.get(5);
		final int g = config.get(6);
		final int h = config.get(7);
		final int i = config.get(8);
		final int j = config.get(9);
		final int k = config.get(10);
		final int l = config.get(11);
		return Arrays.asList(b, l, g, e, i, c, j, d, h, f, a, k);
	}

	private static List<Integer> rotatey(final List<Integer> config) {
		final int a = config.get(0);
		final int b = config.get(1);
		final int c = config.get(2);
		final int d = config.get(3);
		final int e = config.get(4);
		final int f = config.get(5);
		final int g = config.get(6);
		final int h = config.get(7);
		final int i = config.get(8);
		final int j = config.get(9);
		final int k = config.get(10);
		final int l = config.get(11);
		return Arrays.asList(d, c, e, i, g, h, b, j, l, k, f, a);
	}

	private static List<Integer> rotatez(final List<Integer> config) {
		final int a = config.get(0);
		final int b = config.get(1);
		final int c = config.get(2);
		final int d = config.get(3);
		final int e = config.get(4);
		final int f = config.get(5);
		final int g = config.get(6);
		final int h = config.get(7);
		final int i = config.get(8);
		final int j = config.get(9);
		final int k = config.get(10);
		final int l = config.get(11);
		return Arrays.asList(c, e, d, f, h, a, i, k, j, l, b, g);
	}

	private static List<Integer> flipx(final List<Integer> config) {
		final int a = config.get(0);
		final int b = config.get(1);
		final int c = config.get(2);
		final int d = config.get(3);
		final int e = config.get(4);
		final int f = config.get(5);
		final int g = config.get(6);
		final int h = config.get(7);
		final int i = config.get(8);
		final int j = config.get(9);
		final int k = config.get(10);
		final int l = config.get(11);
		return Arrays.asList(d, e, c, a, b, f, g, k, l, j, h, i);
	}

	private static List<Integer> flipy(final List<Integer> config) {
		final int a = config.get(0);
		final int b = config.get(1);
		final int c = config.get(2);
		final int d = config.get(3);
		final int e = config.get(4);
		final int f = config.get(5);
		final int g = config.get(6);
		final int h = config.get(7);
		final int i = config.get(8);
		final int j = config.get(9);
		final int k = config.get(10);
		final int l = config.get(11);
		return Arrays.asList(l, b, g, i, e, j, c, h, d, f, k, a);
	}

	private static List<Integer> flipz(final List<Integer> config) {
		final int a = config.get(0);
		final int b = config.get(1);
		final int c = config.get(2);
		final int d = config.get(3);
		final int e = config.get(4);
		final int f = config.get(5);
		final int g = config.get(6);
		final int h = config.get(7);
		final int i = config.get(8);
		final int j = config.get(9);
		final int k = config.get(10);
		final int l = config.get(11);
		return Arrays.asList(a, k, f, d, h, c, j, e, i, g, b, l);
	}

	private static void outputResult(final List<Integer> nums) {

		final int[] sums = new int[SUM_INDICES.length];
		for(int s = 0 ; s < sums.length ; s++) {
			int sum = 0;
			final int[] sumIndices = SUM_INDICES[s];
			for(int i = 0 ; i < sumIndices.length ; i++) {
				sum += nums.get(sumIndices[i]);
			}
			if(sum != 26) {
				return;
			}
			sums[s] = sum;
		}

		System.out.println(nums.toString() + " - " + Arrays.toString(sums));
		// TODO Auto-generated method stub

	}

}
