package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Credentials;
import co.lightmasters.haunt.model.GenderChoice;
import co.lightmasters.haunt.model.Post;
import co.lightmasters.haunt.model.PostDto;
import co.lightmasters.haunt.model.ProfilePicDto;
import co.lightmasters.haunt.model.Prompt;
import co.lightmasters.haunt.model.PromptDto;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.model.UserFeed;
import co.lightmasters.haunt.model.UserPreferences;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.security.WebSecurityConfig;
import co.lightmasters.haunt.service.UserFeedService;
import co.lightmasters.haunt.service.UserProfileService;
import co.lightmasters.haunt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static co.lightmasters.haunt.model.GenderChoice.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {WebSecurityConfig.class, HauntUserController.class})
public class HauntUserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private UserFeedService userFeedService;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;
    private UserProfile userProfile;
    private Credentials credentials;
    private PostDto postDto;
    private ProfilePicDto profilePicDto;
    private PromptDto promptDto;
    private Prompt prompt;
    private UserFeed feed;

    @BeforeEach
    public void setUp() throws IOException {
        userDto = buildUserDto();
        user = User.from(userDto, "hashed");

        userProfile = buildUserProfile();
        credentials = buildCredentials();
        postDto = buildPostDto();
        promptDto = buildPromptDto();
        prompt = Prompt.from(promptDto);
        feed = UserFeed.builder()
                .posts(Collections.emptyList())
                .username("test")
                .build();
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        profilePicDto=ProfilePicDto.builder()
                .username("test")
                .profilePic(file.getBytes())
                .build();
    }

    @Test
    public void shouldCreateUser() throws Exception {
        when(userService.saveUser(userDto)).thenReturn(user);
        this.mockMvc.perform(post("/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(userDto.toJson()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFetchUserDetailsWhenAvailable() throws Exception {
        when(userService.saveUser(userDto)).thenReturn(user);
        when(userService.fetchUser(user.getUsername())).thenReturn(Optional.ofNullable(user));

        this.mockMvc.perform(post("/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(userDto.toJson()))
                .andExpect(status().isCreated());
        this.mockMvc.perform(get("/v1/user").queryParam("username", "test"))
                .andExpect(status().isOk()).andReturn();

        verify(userService, times(1)).fetchUser(user.getUsername());
    }

    @Test
    public void shouldGive401WhenUserIsNotFound() throws Exception {
        when(userService.fetchUser(user.getUsername())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/v1/user").queryParam("username", "test"))
                .andExpect(status().isUnauthorized()).andReturn();

        verify(userService, times(1)).fetchUser(user.getUsername());
    }

    @Test
    public void shouldCreateUserProfile() throws Exception {
        when(userProfileService.saveProfile(userProfile)).thenReturn(userProfile);
        this.mockMvc.perform(post("/v1/profile").contentType(MediaType.APPLICATION_JSON)
                .content(userProfile.toJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFetchUserProfileWhenAvailable() throws Exception {
        when(userProfileService.fetchProfile(userProfile.getUsername())).thenReturn(Optional.ofNullable(userProfile));
        this.mockMvc.perform(post("/v1/profile").contentType(MediaType.APPLICATION_JSON)
                .content(userProfile.toJson()))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/v1/profile").queryParam("username", "test"))
                .andExpect(status().isOk()).andReturn();

        verify(userProfileService, times(1)).fetchProfile(user.getUsername());
    }

    @Test
    public void shouldGive401WhenUserProfileIsNotFound() throws Exception {
        when(userProfileService.fetchProfile(userProfile.getUsername())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/v1/profile").queryParam("username", "test"))
                .andExpect(status().isUnauthorized()).andReturn();

        verify(userProfileService, times(1)).fetchProfile(user.getUsername());
    }

    @Test
    public void shouldLoginWhenValidCredentialsAreProvided() throws Exception {
        when(userService.saveUser(userDto)).thenReturn(user);
        when(userService.fetchUser(user.getUsername())).thenReturn(Optional.ofNullable(user));

        this.mockMvc.perform(post("/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(userDto.toJson()))
                .andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/login").contentType(MediaType.APPLICATION_JSON)
                .content(credentials.toJson()))
                .andExpect(status().isOk());

        verify(userService, times(1)).loginUser(credentials);
    }

    @Test
    public void shouldSavePostInUsersFeedWhenAvailable() throws Exception {
        when(userFeedService.fetchUserFeed(postDto.getUsername())).thenReturn(Optional.ofNullable(feed));
        this.mockMvc.perform(post("/v1/post").contentType(MediaType.APPLICATION_JSON)
                .content(postDto.toJson()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldSavePostInUsersFeedWhenIsNotAvailable() throws Exception {
        when(userFeedService.fetchUserFeed(postDto.getUsername())).thenReturn(Optional.empty());
        this.mockMvc.perform(post("/v1/post").contentType(MediaType.APPLICATION_JSON)
                .content(postDto.toJson()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFetchLatestPostFromUsersFeed() throws Exception {
        feed.setPosts(Arrays.asList(new Post("firstTweet", new Date()), new Post("secondTweet", new Date())));
        when(userFeedService.getLatestPost(promptDto.getUsername())).thenReturn(feed.getPosts().get(0));
        MvcResult mvcResult = this.mockMvc.perform(get("/v1/post").queryParam("username", "test"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Post post = objectMapper.readValue(contentAsString, Post.class);
        assertEquals(post.getTweet(), "firstTweet");
    }

    @Test
    public void shouldFetchUsersFeed() throws Exception {
        when(userFeedService.fetchUserFeed(promptDto.getUsername())).thenReturn(Optional.ofNullable(feed));
        this.mockMvc.perform(get("/v1/feed").queryParam("username", "test")).andExpect(status().isOk());
    }

    @Test
    public void shouldSavePromptForUser() throws Exception {
        when(userService.savePrompt(promptDto)).thenReturn(prompt);
        this.mockMvc.perform(post("/v1/prompt").contentType(MediaType.APPLICATION_JSON)
                .content(promptDto.toJson()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFetchUsersPrompts() throws Exception {
        when(userService.getPrompts(promptDto.getUsername())).thenReturn(Collections.singletonList(prompt));
        this.mockMvc.perform(get("/v1/prompts").queryParam("username", "test")).andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithNoContentWhenPromptsAreNotAvailable() throws Exception {
        when(userService.getPrompts(promptDto.getUsername())).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/v1/prompts").queryParam("username", "test")).andExpect(status().isNoContent());
    }

    @Test
    public void shouldSaveProfilePicture() throws Exception {
        when(userService.saveProfilePic(profilePicDto)).thenReturn(user);
        this.mockMvc.perform(post("/v1/profilePicture").contentType(MediaType.APPLICATION_JSON)
                .content(profilePicDto.toJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowUnAuthorizedWhenUseIsInvalid() throws Exception {
        when(userService.saveProfilePic(profilePicDto)).thenReturn(null);
        this.mockMvc.perform(post("/v1/profilePicture").contentType(MediaType.APPLICATION_JSON)
                .content(profilePicDto.toJson()))
                .andExpect(status().isUnauthorized());
    }

    private UserProfile buildUserProfile() {
        UserPreferences userPreferences = UserPreferences.builder().activePeopleStatus("").genderChoice("Both").politicalOpinion("").build();
        return UserProfile.builder()
                .username("test")
                .drinking(false)
                .smoking(false)
                .education("Schooling")
                .height("5")
                .organization("")
                .profession("")
                .religion("")
                .sexuality("Straight")
                .starSign("Zodiac")
                .student(false)
                .userPreferences(userPreferences)
                .build();
    }

    private UserDto buildUserDto() {
        return UserDto.builder()
                .username("test")
                .password("password")
                .firstName("first")
                .lastName("last")
                .age(21)
                .gender(MALE.toString())
                .city("city")
                .aboutMe("Duh")
                .build();
    }

    private Credentials buildCredentials() {
        return Credentials.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    private PromptDto buildPromptDto() {
        return PromptDto.builder()
                .question("question")
                .answer("answer")
                .username("test")
                .build();
    }

    private PostDto buildPostDto() {
        return PostDto.builder()
                .tweet("tweet")
                .username("test")
                .build();
    }
}
