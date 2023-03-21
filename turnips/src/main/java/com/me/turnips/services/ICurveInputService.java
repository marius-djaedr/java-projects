package com.me.turnips.services;

import java.util.List;

import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.UserInput;

public interface ICurveInputService {
	List<CostCurve> getCurves(UserInput userInput);
}
