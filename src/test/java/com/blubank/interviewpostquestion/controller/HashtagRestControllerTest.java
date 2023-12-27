package com.blubank.interviewpostquestion.controller;

import com.blubank.interviewpostquestion.service.HashtagService;
import com.blubank.interviewpostquestion.service.api.hashtag.HashtagModel;
import com.blubank.interviewpostquestion.service.api.hashtag.HashtagRankModel;
import com.blubank.interviewpostquestion.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(HashtagRestController.class)
public class HashtagRestControllerTest extends AbstractRestControllerTest {

	@MockBean
	private HashtagService service;

	@Test
	public void test_hashtag_load__restController() throws Exception {
		final String hashtag = "#BLUBANK";

		// mock service
		HashtagModel result = HashtagModel.builder()
				.hashtag(hashtag.toLowerCase())
				.tweetIds(TestUtil.setOf(1L, 2L, 3L))
				.build();
		Mockito.when(service.findByHashtag(hashtag)).thenReturn(result);

		mvc.perform(get("/hashtags/{0}", hashtag)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.hashtag").value(result.getHashtag()))
				.andExpect(jsonPath("$.tweetIds", hasSize(result.getTweetIds().size())))
				.andExpect(jsonPath("$.tweetIds", hasItem(1)))
				.andExpect(jsonPath("$.tweetIds", hasItem(2)))
				.andExpect(jsonPath("$.tweetIds", hasItem(3)));
	}

	@Test
	public void test_hashtag_topTrends__restController() throws Exception {
		// mock service
		HashtagRankModel blubank = createHashtagRank("#blubank", 100);
		HashtagRankModel contest = createHashtagRank("#contest", 59);
		HashtagRankModel programmer = createHashtagRank("#programmer", 34);
		HashtagRankModel developer = createHashtagRank("#developer", 12);
		List<HashtagRankModel> results = TestUtil.listOf(blubank, contest, programmer, developer);
		Mockito.when(service.topTrends(10, 7)).thenReturn(results);

		mvc.perform(get("/hashtags/top-trends")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items", hasSize(results.size())))
				.andExpect(jsonPath("$.items[0].count").value(blubank.getCount()))
				.andExpect(jsonPath("$.items[0].hashtag").value(blubank.getHashtag()))
				.andExpect(jsonPath("$.items[3].count").value(developer.getCount()))
				.andExpect(jsonPath("$.items[3].hashtag").value(developer.getHashtag()));
	}

	private HashtagRankModel createHashtagRank(String hashtag, int count) {
		return HashtagRankModel.builder()
				.count(count)
				.hashtag(hashtag)
				.build();
	}

}