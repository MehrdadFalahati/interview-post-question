package com.blubank.interviewpostquestion.service.api.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetLikeResult {

	boolean success;

	public static TweetLikeResult success() {
		return TweetLikeResult.builder().success(true).build();
	}

	public static TweetLikeResult failure() {
		return TweetLikeResult.builder().success(false).build();
	}

}
