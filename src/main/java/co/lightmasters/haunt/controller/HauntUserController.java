package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Credentials;
import co.lightmasters.haunt.model.Post;
import co.lightmasters.haunt.model.PostDto;
import co.lightmasters.haunt.model.Prompt;
import co.lightmasters.haunt.model.PromptDto;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.model.UserFeed;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.model.UserResponse;
import co.lightmasters.haunt.service.UserFeedService;
import co.lightmasters.haunt.service.UserProfileService;
import co.lightmasters.haunt.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class HauntUserController {
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final UserFeedService userFeedService;

    @GetMapping(path = "/user")
    public ResponseEntity<UserResponse> fetchUser(@RequestParam @Valid String username) {
        Optional<User> user = userService.fetchUser(username);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    @PostMapping(path = "/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserDto userDto) {
        User saveUser = userService.saveUser(userDto);
        UserResponse userResponse = UserResponse.from(saveUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping(path = "/profile")
    public ResponseEntity<UserProfile> fetchUserProfile(@RequestParam @Valid String username) {
        Optional<UserProfile> profile = userProfileService.fetchProfile(username);
        return profile.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    @PostMapping(path = "/profile")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody @Valid UserProfile userProfile) {
        UserProfile saveProfile = userProfileService.saveProfile(userProfile);
        return ResponseEntity.status(HttpStatus.OK).body(saveProfile);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid Credentials credentials) {
        return userService.loginUser(credentials);
    }

    @PostMapping(path = "/post")
    public ResponseEntity<Post> savePost(@RequestBody @Valid PostDto post) {
        Post savePost = userFeedService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savePost);
    }

    @GetMapping(path = "/post")
    public ResponseEntity<Post> getLatestPost(@RequestParam @Valid String username) {
        Post latestPost = userFeedService.getLatestPost(username);
        return ResponseEntity.status(HttpStatus.OK).body(latestPost);
    }

    @GetMapping(path = "/feed")
    public ResponseEntity<UserFeed> getUserFeed(@RequestParam @Valid String username) {
        Optional<UserFeed> userFeed = userFeedService.fetchUserFeed(username);
        return userFeed.map(feed -> ResponseEntity.status(HttpStatus.OK).body(feed))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PostMapping(path = "/prompt")
    public ResponseEntity<Prompt> savePrompt(@RequestBody @Valid PromptDto promptDto) {
        Prompt prompt = userService.savePrompt(promptDto);
        if (prompt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(prompt);
    }

    @GetMapping(path = "/prompts")
    public ResponseEntity<List<Prompt>> getUserPrompts(@RequestParam @Valid String username) {
        List<Prompt> prompts = userService.getPrompts(username);
        if (prompts == null || prompts.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(prompts);
    }
}
