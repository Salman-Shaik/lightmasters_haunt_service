package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Post;
import co.lightmasters.haunt.model.UserFeed;
import co.lightmasters.haunt.repository.UserFeedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserFeedServiceTest {
    UserFeed userFeed;
    UserFeedRepository userFeedRepository;
    UserFeedService userFeedService;

    @BeforeEach
    void setUp() {
        userFeed = UserFeed.builder()
                .username("test")
                .posts(Collections.emptyList())
                .build();
        userFeedRepository = mock(UserFeedRepository.class);
        userFeedService = new UserFeedService(userFeedRepository);
    }

    @Test
    void shouldGetLatestPost() {
        Post firstTweet = new Post("firstTweet", new Date());
        Post secondTweet = new Post("secondTweet", new Date());

        userFeed.setPosts(Arrays.asList(firstTweet, secondTweet));
        when(userFeedRepository.findById("test")).thenReturn(java.util.Optional.ofNullable(userFeed));
        Post latestPost = userFeedService.getLatestPost("test");
        Assertions.assertEquals(latestPost.getTweet(), "firstTweet");
    }

    @Test
    void shouldFetchUserFeed() {
        when(userFeedRepository.findById("test")).thenReturn(java.util.Optional.ofNullable(userFeed));
        Optional<UserFeed> feed = userFeedService.fetchUserFeed("test");
        assertEquals(feed.get(), userFeed);
    }
}
