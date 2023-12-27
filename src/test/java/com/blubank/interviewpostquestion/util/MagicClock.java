package com.blubank.interviewpostquestion.util;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class MagicClock implements Clock {

	private long currentTime;

	@Override
	public long currentTime() {
		return currentTime;
	}

}
