package com.blubank.interviewpostquestion.controller;

import com.blubank.interviewpostquestion.controller.api.tweet.TweetLikeRequest;
import com.blubank.interviewpostquestion.controller.api.tweet.TweetSaveRequest;
import com.blubank.interviewpostquestion.service.TweetService;
import com.blubank.interviewpostquestion.service.api.tweet.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(TweetRestController.class)
public class TweetRestControllerTest extends AbstractRestControllerTest {

	@MockBean
	private TweetService service;

	@Test
	public void test_tweet_save__restController() throws Exception {
		TweetSaveRequest request = new TweetSaveRequest();
		request.setAuthor("Kambiz");
		request.setContent("Hello Worlds!");

		// mock service
		TweetSaveResult serviceResult = TweetSaveResult.builder().tweetId(10).build();
		Mockito.when(service.save(any())).thenReturn(serviceResult);

		mvc.perform(post("/tweets")
				.content(toJson(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.tweetId").value(serviceResult.getTweetId()));
	}

	@Test
	public void test_tweet_load__restController() throws Exception {
		// mock service
		TweetLoadParam loadParam = TweetLoadParam.builder()
				.includeHashtags(true)
				.includeLikeCount(false)
				.tweetId(5)
				.build();
		TweetModel loadResult = TweetModel.builder()
				.author("Reza")
				.content("Hello #blubank #contest")
				.hashtags(Set.of("#blubank", "#contest"))
				.time(12345)
				.likeCount(0)
				.tweetId(loadParam.getTweetId())
				.build();
		Mockito.when(service.load(loadParam)).thenReturn(loadResult);

		mvc.perform(get("/tweets/{0}", loadParam.getTweetId())
				.param("includeHashtags", "true")
				.param("includeLikeCount", "false")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.tweetId").value(loadResult.getTweetId()))
				.andExpect(jsonPath("$.author").value(loadResult.getAuthor()))
				.andExpect(jsonPath("$.content").value(loadResult.getContent()))
				.andExpect(jsonPath("$.time").value(loadResult.getTime()))
				.andExpect(jsonPath("$.hashtags", hasSize(2)))
				.andExpect(jsonPath("$.hashtags", hasItem("#blubank")))
				.andExpect(jsonPath("$.hashtags", hasItem("#contest")))
				.andExpect(jsonPath("$.likeCount").value(loadResult.getLikeCount()));
	}

	@Test
	public void test_tweet_like__restController() throws Exception {
		TweetLikeRequest likeRequest = new TweetLikeRequest();
		likeRequest.setLikedBy("Ahmad");

		// mock service
		TweetLikeParam likeParam = TweetLikeParam.builder()
				.likedBy(likeRequest.getLikedBy())
				.tweetId(3)
				.build();
		TweetLikeResult likeResult = TweetLikeResult.builder().success(true).build();
		Mockito.when(service.like(likeParam)).thenReturn(likeResult);

		mvc.perform(post("/tweets/{0}/like", likeParam.getTweetId())
				.content(toJson(likeRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(likeResult.isSuccess()));
	}

}