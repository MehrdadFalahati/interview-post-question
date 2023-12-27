package com.blubank.interviewpostquestion.service.api.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetModel {

	private long tweetId;
	private String author;
	private String content;
	private long time;

	private Set<String> hashtags;
	private int likeCount;

}
