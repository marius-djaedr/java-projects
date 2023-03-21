package com.me.turnips.processors;

import com.me.turnips.dtos.CostCurve;

public interface IEachCurveProcessor extends ICurveProcessor {
	void process(CostCurve curve);
}
