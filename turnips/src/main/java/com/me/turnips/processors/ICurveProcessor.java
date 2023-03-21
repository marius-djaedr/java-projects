package com.me.turnips.processors;

import com.me.turnips.dtos.UserInput;

public interface ICurveProcessor {
	void initialize(UserInput userInput);

	void output();
}
