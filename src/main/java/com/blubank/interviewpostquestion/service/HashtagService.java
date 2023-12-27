package com.blubank.interviewpostquestion.service;

import com.blubank.interviewpostquestion.service.api.hashtag.HashtagModel;
import com.blubank.interviewpostquestion.service.api.hashtag.HashtagRankModel;

import java.util.List;

public interface HashtagService {
    HashtagModel findByHashtag(String hashtag);

    List<HashtagRankModel> topTrends(int resultLimit, int recentDays);
}
