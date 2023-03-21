package com.me.turnips.processors;

import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.UserInput;

//TODO currently turned off
public class BuyCurveProcessor implements IEachCurveProcessor {
	//	private Map<CostCurve, BigDecimal> curveMax;
	//	private Map<CostCurve, DayTime> latestLocationOfMax;
	///////////////////////
	//	private BigDecimal probBuyToday;

	@Override
	public void initialize(final UserInput userInput) {
		//		curveMax = new HashMap<>();
		//		latestLocationOfMax = new HashMap<>();
		/////////////////////////
		//		probBuyToday = BigDecimal.ZERO;
	}

	//	@Override
	//	public void process(final CostCurve curve, final DayTime dayTime, final DailyCost curveCost) {
	//		if(dayTime.ordinal() >= InputConstants.currentDayTime.ordinal()) {
	//			final BigDecimal currentMax = curveMax.getOrDefault(curve, BigDecimal.ZERO);
	//			final BigDecimal average = curveCost.average();
	//			if(average != null) {
	//				if(average.compareTo(currentMax) > 0) {
	//					curveMax.put(curve, average);
	//					latestLocationOfMax.put(curve, dayTime);
	//				} else if(average.compareTo(currentMax) == 0 && dayTime.ordinal() > latestLocationOfMax.get(curve).ordinal()) {
	//					latestLocationOfMax.put(curve, dayTime);
	//				}
	//				//else do nothing
	//			}
	//		}
	//	}
	////////////////////////////////////////////////////////////////////////////
	@Override
	public void process(final CostCurve curve) {
		//for each curve, need the following for each day today and later
		//  - probability buy that day
		//  - potential loss for that day

		// for spikes:
		//  - find the day for maximum
		//  - probability buy that day is 1, all else are 0
		//  - loss for that day is 0, all else are value for that day subtracted from the maximum value

		//for decreasing:
		//  - probability buy today is 1, all else are 0
		//  - loss for every day but today is that day's value subtracted from today's value

		// from this, spikes and decreasing can be treated the same, knowing that decreasing's maximum value is today

		//for fluctuating:
		//  - set today's value as a threshold
		//  - for each day, calculate the percent of that range that is equal or above threshold, that is probability buy that day
		//  - ?? how to calculate loss? what is used as the "max value" that the others have? should it be today? should it be calculated from buy values?

		//////////////////
		//		final CurveType curveType = curve.getCurveType();
		//
		//		switch(curveType) {
		//			case LARGE_SPIKE:
		//			case SMALL_SPIKE:
		//				processSpike(curve);
		//				break;
		//			case DECREASING:
		//				processDecreasing(curve);
		//				break;
		//			case FLUCTUATING:
		//				processFluctuating(curve);
		//				break;
		//			case UNKNOWN:
		//			default:
		//				throw new NotImplementedException("A new curve type has been defined but the logic is missing here");
		//		}
	}
	//
	//	private void processDecreasing(final CostCurve curve) {
	//		probBuyToday = probBuyToday.add(curve.getProbability());
	//		// TODO Auto-generated method stub
	//	}
	//
	//	private void processSpike(final CostCurve curve) {
	//		final boolean todayIsSpike = false;//TODO
	//		if(todayIsSpike) {
	//			probBuyToday = probBuyToday.add(curve.getProbability());
	//		}
	//		// TODO Auto-generated method stub
	//	}
	//
	//	private void processFluctuating(final CostCurve curve) {
	//		// TODO Auto-generated method stub
	//	}
	////////////////////////////////////////////////////////////////////////////

	//	@Override
	//	public void process(final Collection<CostCurve> curves) {
	//		final Map<CurveType, List<CostCurve>> curveTypeMap = curves.stream().collect(Collectors.groupingBy(CostCurve::getCurveType));
	//
	//		final List<CostCurve> buyTodayCurves = new ArrayList<>();
	//		final List<CostCurve> buyLaterCurves = new ArrayList<>();
	//
	//		curveTypeMap.getOrDefault(CurveType.DECREASING, new ArrayList<>()).forEach(buyTodayCurves::add);
	//
	//		final Map<Boolean, List<CostCurve>> fluctuatingCurveBuyTodayMapping = curveTypeMap.getOrDefault(CurveType.FLUCTUATING, new ArrayList<>())
	//				.stream().collect(Collectors.groupingBy(this::shouldFluctuatingBuyToday));
	//		buyTodayCurves.addAll(fluctuatingCurveBuyTodayMapping.get(true));
	//		buyTodayCurves.addAll(fluctuatingCurveBuyTodayMapping.get(false));
	//
	//		fluctuatingCurveBuyTodayMapping.get(null);//TODO the complicated ones, where som
	//
	//		// TODO Auto-generated method stub
	//
	//	}
	//
	//private Boolean shouldFluctuatingBuyToday(final CostCurve fluctuating) {
	//
	//}
	@Override
	public void output() {
		//		final Set<CostCurve> curves = curveMax.keySet();
		//
		//		final EnumMap<DayTime, BigDecimal> filteredExpectedMaxes = new EnumMap<>(DayTime.class);
		//		BigDecimal totalE = BigDecimal.ZERO;
		//
		//		for(final CostCurve curve : curves) {
		//			final DayTime dayTime = latestLocationOfMax.get(curve);
		//			final BigDecimal toAdd = curveMax.get(curve).multiply(curve.getProbability());
		//
		//			final BigDecimal sum = filteredExpectedMaxes.getOrDefault(dayTime, BigDecimal.ZERO).add(toAdd);
		//			filteredExpectedMaxes.put(dayTime, sum);
		//
		//			totalE = totalE.add(toAdd);
		//		}

		// TODO output to the sheet

	}

}
