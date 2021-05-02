package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Date;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.model.UserPreferences;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.repository.UserProfileRepository;
import co.lightmasters.haunt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateServiceTest {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private DateService dateService;

    private User user;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userProfileRepository = mock(UserProfileRepository.class);
        dateService = new DateService(userRepository, userProfileRepository);
        user = User.from(buildUserDto("test"), "hashed");
        userProfile = buildUserProfile();
    }

    @Test
    void shouldReturnEmptyListWhenUserIsNotValid() {
        when(userRepository.findById("test")).thenReturn(Optional.empty());
        List<Date> dateSuggestions = dateService.getDateSuggestions("test");
        assertTrue(dateSuggestions.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenUserProfileIsNotAvailable() {
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllByCity(user.getCity())).thenReturn(Collections.singletonList(user));
        when(userProfileRepository.findById("test")).thenReturn(Optional.empty());

        List<Date> dateSuggestions = dateService.getDateSuggestions("test");
        assertTrue(dateSuggestions.isEmpty());
    }

    @Test
    void shouldReturnDateSuggestions() {
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(user));
        User suggestion = User.from(buildUserDto("suggestion"), "hashed");
        when(userRepository.findAllByCity(user.getCity())).thenReturn(Collections.singletonList(suggestion));
        when(userProfileRepository.findById("suggestion")).thenReturn(Optional.ofNullable(userProfile));

        List<Date> dateSuggestions = dateService.getDateSuggestions("test");
        assertFalse(dateSuggestions.isEmpty());
        assertEquals(dateSuggestions.get(0).getCity(), user.getCity());
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

    private UserDto buildUserDto(String username) {
        return UserDto.builder()
                .username(username)
                .password("password")
                .firstName("first")
                .lastName("last")
                .age(21)
                .gender("Male")
                .city("city")
                .aboutMe("Duh")
                .build();
    }
}
