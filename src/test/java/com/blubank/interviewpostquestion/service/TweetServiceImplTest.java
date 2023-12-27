package com.blubank.interviewpostquestion.service;

import com.blubank.interviewpostquestion.service.api.tweet.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertFalse;


@DataJpaTest
@ActiveProfiles("test")
public class TweetServiceImplTest {

	@Autowired
	private TweetService tweetService;

	@TestConfiguration
	@ComponentScan("com.blubank.interviewpostquestion")
	public static class TweetServiceImplTestConfiguration {
	}

	@Test
	public void test_tweet_save__service() {
		TweetSaveParam param = TweetSaveParam.builder()
				.content("The #game begins where you end and ends where you began.")
				.author("Dr. Ford")
				.build();
		TweetSaveResult result = tweetService.save(param);
		assertTrue(result.getTweetId() > 0);
	}

	@Test
	public void test_tweet_load_includeHashtags_notIncludeLikeCount__service() {
		TweetSaveParam saveParam = TweetSaveParam.builder()
				.content("The #piano doesn’t murder the player if it doesn’t like the #music.")
				.author("Dr. Ford")
				.build();
		TweetSaveResult saveResult = tweetService.save(saveParam);

		TweetLoadParam loadParam = TweetLoadParam.builder()
				.tweetId(saveResult.getTweetId())
				.includeHashtags(true)
				.includeLikeCount(false)
				.build();
		TweetModel tweet = tweetService.load(loadParam);
		assertEquals(saveResult.getTweetId(), tweet.getTweetId());
		assertEquals(saveParam.getAuthor(), tweet.getAuthor());
		assertEquals(saveParam.getContent(), tweet.getContent());
		assertTrue(tweet.getTime() > 0);
		assertEquals( 2, tweet.getHashtags().size(), "'hashtags' included");
		assertTrue(tweet.getHashtags().contains("#piano"));
		assertTrue(tweet.getHashtags().contains("#music"));
		assertEquals(0, tweet.getLikeCount(), "'likeCount' not included");
	}

	@Test
	public void test_tweet_load_notIncludeHashtags_includeLikeCount__service() {
		TweetSaveParam saveParam = TweetSaveParam.builder()
				.content("The #piano doesn’t murder the player if it doesn’t like the #music.")
				.author("Dr. Ford")
				.build();
		TweetSaveResult saveResult = tweetService.save(saveParam);

		TweetLikeResult likeResult = tweetService.like(TweetLikeParam.builder()
				.tweetId(saveResult.getTweetId())
				.likedBy("Bernard")
				.build());
		assertTrue(likeResult.isSuccess());

		TweetLoadParam loadParam = TweetLoadParam.builder()
				.tweetId(saveResult.getTweetId())
				.includeHashtags(false)
				.includeLikeCount(true)
				.build();
		TweetModel tweet = tweetService.load(loadParam);
		assertEquals(saveResult.getTweetId(), tweet.getTweetId());
		assertEquals(saveParam.getAuthor(), tweet.getAuthor());
		assertEquals(saveParam.getContent(), tweet.getContent());
		assertTrue(tweet.getTime() > 0);
		assertEquals(0, tweet.getHashtags().size(), "'hashtags' not included");
		assertEquals(1, tweet.getLikeCount(), "'likeCount' included");
	}

	@Test
	public void test_tweet_like__service() {
		TweetSaveParam param = TweetSaveParam.builder()
				.content("The only time a man can be #brave is when he is #afraid!")
				.author("Eddard")
				.build();
		TweetSaveResult saveResult = tweetService.save(param);

		TweetLikeResult likeResult = tweetService.like(TweetLikeParam.builder()
				.tweetId(saveResult.getTweetId())
				.likedBy("Jon")
				.build());
		assertTrue(likeResult.isSuccess(), "Liked by Jon for the first time.");

		likeResult = tweetService.like(TweetLikeParam.builder()
				.tweetId(saveResult.getTweetId())
				.likedBy("Jon")
				.build());
		assertFalse("Liked by Jon for the second time.", likeResult.isSuccess());
	}

}