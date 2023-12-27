package com.blubank.interviewpostquestion.service.api.hashtag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashtagRankModel {

	private String hashtag;
	private long count;

}
