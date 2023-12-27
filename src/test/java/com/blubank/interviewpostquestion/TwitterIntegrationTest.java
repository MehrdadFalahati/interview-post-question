package com.blubank.interviewpostquestion;


import com.blubank.interviewpostquestion.controller.api.hashtag.HashtagTrendResponse;
import com.blubank.interviewpostquestion.controller.api.tweet.TweetLikeRequest;
import com.blubank.interviewpostquestion.controller.api.tweet.TweetLikeResponse;
import com.blubank.interviewpostquestion.controller.api.tweet.TweetSaveRequest;
import com.blubank.interviewpostquestion.controller.api.tweet.TweetSaveResponse;
import com.blubank.interviewpostquestion.service.api.hashtag.HashtagModel;
import com.blubank.interviewpostquestion.service.api.tweet.TweetModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TwitterIntegrationTest {

    @LocalServerPort
    int serverPort;

    @Test
    public void test_tweet_save_like_load__integration() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // save
        TweetSaveRequest saveRequest = createTweetSaveReq("Walter", "I am #Heisenberg! I am the one who knocks.");
        TweetSaveResponse saveResponse = restTemplate.postForObject(createUrl("/tweets"), saveRequest, TweetSaveResponse.class);
        assertTrue(saveResponse.getTweetId() > 0);

        // like
        TweetLikeRequest likeRequest = new TweetLikeRequest();
        likeRequest.setLikedBy("Jesse");
        TweetLikeResponse likeResponse = restTemplate.postForObject(createUrl("/tweets/{0}/like"), likeRequest, TweetLikeResponse.class, saveResponse.getTweetId());
        assertTrue(likeResponse.isSuccess());

        // load
        TweetModel loadResponse = restTemplate.getForObject(createUrl("/tweets/{0}?includeHashtags={1}&includeLikeCount={2}"), TweetModel.class, saveResponse.getTweetId(), true, true);
        assertEquals(saveRequest.getAuthor(), loadResponse.getAuthor());
        assertEquals(saveRequest.getContent(), loadResponse.getContent());
        assertEquals(1, loadResponse.getHashtags().size());
        assertEquals(1, loadResponse.getLikeCount());
        assertTrue(loadResponse.getTime() > 0);
        assertEquals(saveResponse.getTweetId(), loadResponse.getTweetId());
    }

    @Test
    public void test_hashtag_saveTweets_findByHashtag_topTrends__integration() {
        // save tweets
        invokeSaveTweet(createTweetSaveReq("Tyrion", "A mind needs #books like a #sword needs a whetstone."));
        TweetSaveResponse varys = invokeSaveTweet(createTweetSaveReq("Varys", "#Chaos? A gaping #pit waiting to swallow us all."));
        TweetSaveResponse petyr = invokeSaveTweet(createTweetSaveReq("Petyr", "@Varys: #Chaos isn't a pit. Chaos is a #ladder."));
        invokeSaveTweet(createTweetSaveReq("Arya", "I am no #one."));

        TestRestTemplate restTemplate = new TestRestTemplate();

        // find tweets by hashtag
        String toBeFoundHashtag = "#chaos";
        HashtagModel findHashtagResponse = restTemplate.getForObject(createUrl("/hashtags/{0}"), HashtagModel.class, toBeFoundHashtag);
        assertEquals(toBeFoundHashtag, findHashtagResponse.getHashtag());
        assertEquals(2, findHashtagResponse.getTweetIds().size());
        assertTrue(findHashtagResponse.getTweetIds().contains(varys.getTweetId()));
        assertTrue(findHashtagResponse.getTweetIds().contains(petyr.getTweetId()));

        // hashtag trends
        HashtagTrendResponse trendResponse = restTemplate.getForObject(createUrl("/hashtags/top-trends"), HashtagTrendResponse.class);
        assertEquals(6, trendResponse.getItems().size());
        assertEquals("#chaos", trendResponse.getItems().get(0).getHashtag()); // first rank
    }

    private TweetSaveRequest createTweetSaveReq(String author, String content) {
        TweetSaveRequest saveRequest = new TweetSaveRequest();
        saveRequest.setAuthor(author);
        saveRequest.setContent(content);
        return saveRequest;
    }

    private TweetSaveResponse invokeSaveTweet(TweetSaveRequest request) {
        TestRestTemplate restTemplate = new TestRestTemplate();
        return restTemplate.postForObject(createUrl("/tweets"), request, TweetSaveResponse.class);
    }

    private String createUrl(String path) {
        return "http://localhost:" + serverPort + path;
    }
}
