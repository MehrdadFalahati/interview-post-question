package com.blubank.interviewpostquestion.service.api.hashtag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashtagModel {

	private String hashtag;
	private Set<Long> tweetIds;

}
