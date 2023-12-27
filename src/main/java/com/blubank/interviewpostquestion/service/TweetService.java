package com.blubank.interviewpostquestion.service;

import com.blubank.interviewpostquestion.service.api.tweet.*;

public interface TweetService {

    TweetSaveResult save(TweetSaveParam param);

    TweetLikeResult like(TweetLikeParam param);

    TweetModel load(TweetLoadParam param);
}
