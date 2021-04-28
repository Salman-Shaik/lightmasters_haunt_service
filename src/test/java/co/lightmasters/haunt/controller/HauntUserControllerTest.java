package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Credentials;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.model.UserPreferences;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.model.UserResponse;
import co.lightmasters.haunt.security.WebSecurityConfig;
import co.lightmasters.haunt.service.UserProfileService;
import co.lightmasters.haunt.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

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

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;
    private UserProfile userProfile;
    private Credentials credentials;

    @BeforeEach
    public void setUp() {
        userDto = buildUserDto();
        user = User.from(userDto, "hashed");

        userProfile = buildUserProfile();
        credentials= Credentials.builder()
                .username(user.getUsername())
                .password(user.getPassword())
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
                .gender("Male")
                .aboutMe("Duh")
                .build();
    }
}