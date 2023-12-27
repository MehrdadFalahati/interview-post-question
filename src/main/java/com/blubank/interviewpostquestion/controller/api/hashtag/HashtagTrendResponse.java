package com.blubank.interviewpostquestion.controller.api.hashtag;


import com.blubank.interviewpostquestion.service.api.hashtag.HashtagRankModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashtagTrendResponse {

	private List<HashtagRankModel> items;

}
