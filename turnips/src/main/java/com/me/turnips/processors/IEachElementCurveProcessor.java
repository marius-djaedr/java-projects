package com.me.turnips.processors;

import com.me.turnips.constants.DayTime;
import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.DailyCost;

public interface IEachElementCurveProcessor extends ICurveProcessor {

	void process(CostCurve curve, DayTime dayTime, DailyCost curveCost);

}
