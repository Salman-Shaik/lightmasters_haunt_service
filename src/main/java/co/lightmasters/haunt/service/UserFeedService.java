package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Post;
import co.lightmasters.haunt.model.PostDto;
import co.lightmasters.haunt.model.ProfilePicDto;
import co.lightmasters.haunt.model.Prompt;
import co.lightmasters.haunt.model.PromptDto;
import co.lightmasters.haunt.model.UserFeed;
import co.lightmasters.haunt.repository.UserFeedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserFeedService {
    public final UserFeedRepository userFeedRepository;

    public Post savePost(@Valid PostDto postDto) {
        String username = postDto.getUsername();
        Post post = Post.from(postDto);
        Optional<UserFeed> userFeed = fetchUserFeed(username);
        UserFeed feed;
        if (userFeed.isPresent()) {
            feed = userFeed.get();
            feed.addPost(post);
        } else {
            feed = UserFeed.builder()
                    .posts(Collections.singletonList(post))
                    .username(username)
                    .build();
        }
        userFeedRepository.save(feed);
        return post;
    }

    public Post getLatestPost(String username) {
        Optional<UserFeed> userFeed = fetchUserFeed(username);
        if (userFeed.isPresent()) {
            UserFeed feed = userFeed.get();
            List<Post> posts = feed.getPosts();
            posts.sort(Comparator.comparing(Post::getTimeOfCreation));
            return posts.get(0);
        }
        return new Post();
    }

    public Optional<UserFeed> fetchUserFeed(String username) {
        return userFeedRepository.findById(username);
    }
}
