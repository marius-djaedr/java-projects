package com.me.turnips.processors;

import java.util.Collection;

import com.me.turnips.dtos.CostCurve;

public interface IBulkCurveProcessor extends ICurveProcessor {
	void process(Collection<CostCurve> curves);
}
