package com.blubank.interviewpostquestion.service;

import com.blubank.interviewpostquestion.service.api.hashtag.HashtagModel;
import com.blubank.interviewpostquestion.service.api.hashtag.HashtagRankModel;
import com.blubank.interviewpostquestion.service.api.tweet.TweetSaveParam;
import com.blubank.interviewpostquestion.service.api.tweet.TweetSaveResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@ActiveProfiles("test")
public class HashtagServiceImplTest {

	@Autowired
	private TweetService tweetService;
	@Autowired
	private HashtagService hashtagService;

	@TestConfiguration
	@ComponentScan("com.blubank.interviewpostquestion")
	public static class HashtagServiceImplTestConfiguration {
	}

	@Test
	public void test_hashtag_findByHashtag__service() {
		TweetSaveResult result1 = saveTweet("Reza", "#Blubank", "#blubank", "#contest");
		TweetSaveResult result2 = saveTweet("Ahmad", "#programming", "#blubank");

		HashtagModel model = hashtagService.findByHashtag("#BLUBANK");
		assertEquals("#blubank", model.getHashtag());
		assertEquals(2, model.getTweetIds().size());
		assertTrue(model.getTweetIds().contains(result1.getTweetId()));
		assertTrue(model.getTweetIds().contains(result2.getTweetId()));
	}

	@Test
	public void test_hashtag_topTrends__service() {
		saveTweet("Reza", "#developer", "#blubank", "#contest");
		saveTweet("Ahmad", "#programming", "#blubank", "#contest");
		saveTweet("Maryam", "#article", "#blubank");

		// We have 5 distinct hashtags, but the limit is 2
		List<HashtagRankModel> ranks = hashtagService.topTrends(2, 5);
		assertEquals(2, ranks.size());
		assertEquals("#blubank", ranks.get(0).getHashtag());
		assertEquals(3, ranks.get(0).getCount());
		assertEquals("#contest", ranks.get(1).getHashtag());
		assertEquals(2, ranks.get(1).getCount());
	}

	private TweetSaveResult saveTweet(String author, String... hashtags) {
		return tweetService.save(TweetSaveParam.builder()
				.author(author)
				.content("Hello " + String.join(" ", hashtags))
				.build());
	}

}