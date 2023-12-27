package com.blubank.interviewpostquestion.util.impl;

import com.blubank.interviewpostquestion.util.Clock;
import org.springframework.stereotype.Component;

@Component
public class SystemClock implements Clock {
	@Override
	public long currentTime() {
		return System.currentTimeMillis();
	}
}
