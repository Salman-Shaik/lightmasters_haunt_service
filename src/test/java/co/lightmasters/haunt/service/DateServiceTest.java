package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Date;
import co.lightmasters.haunt.model.GenderChoice;
import co.lightmasters.haunt.model.Match;
import co.lightmasters.haunt.model.SwipeDto;
import co.lightmasters.haunt.model.SwipeResponse;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.model.UserPreferences;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.repository.IgnoreRepository;
import co.lightmasters.haunt.repository.MatchRepository;
import co.lightmasters.haunt.repository.UserProfileRepository;
import co.lightmasters.haunt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static co.lightmasters.haunt.model.GenderChoice.FEMALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateServiceTest {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private MatchRepository matchRepository;
    private IgnoreRepository ignoreRepository;
    private DateService dateService;

    private User user;
    private UserProfile userProfile;
    private SwipeDto swipeDto;
    private Match match;
    private Match existingMatch;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userProfileRepository = mock(UserProfileRepository.class);
        matchRepository = mock(MatchRepository.class);
        ignoreRepository = mock(IgnoreRepository.class);
        dateService = new DateService(userRepository, userProfileRepository, matchRepository, ignoreRepository);

        user = User.from(buildUserDto("test"), "hashed");
        userProfile = buildUserProfile();
        swipeDto = SwipeDto.builder()
                .username("test")
                .swipedUserName("swiped")
                .build();
        match = Match.from(swipeDto);
        existingMatch = Match.builder().swipedUsername(swipeDto.getUsername()).username(swipeDto.getSwipedUserName()).build();
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
        when(userRepository.findByCity(user.getCity())).thenReturn(Collections.singletonList(user));
        when(userProfileRepository.findById("test")).thenReturn(Optional.empty());

        List<Date> dateSuggestions = dateService.getDateSuggestions("test");
        assertTrue(dateSuggestions.isEmpty());
    }

    @Test
    void shouldReturnDateSuggestionsWithBothGenderPreference() {
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(user));
        when(userProfileRepository.findById("test")).thenReturn(Optional.ofNullable(userProfile));
        User suggestion = User.from(buildUserDto("suggestion"), "hashed");
        when(userRepository.findByCity(user.getCity())).thenReturn(Collections.singletonList(suggestion));
        when(userProfileRepository.findById("suggestion")).thenReturn(Optional.ofNullable(userProfile));

        List<Date> dateSuggestions = dateService.getDateSuggestions("test");
        assertFalse(dateSuggestions.isEmpty());
        assertEquals(dateSuggestions.get(0).getCity(), user.getCity());
    }

    @Test
    void shouldReturnDateSuggestionsWithABinaryGenderPreference() {
        userProfile.setGenderChoice(FEMALE.toString());
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(user));
        when(userProfileRepository.findById("test")).thenReturn(Optional.ofNullable(userProfile));
        User suggestion = User.from(buildUserDto("suggestion"), "hashed");
        when(userRepository.findByCityAndGender(user.getCity(), userProfile.getGenderChoice())).thenReturn(Collections.singletonList(suggestion));
        when(userProfileRepository.findById("suggestion")).thenReturn(Optional.ofNullable(userProfile));

        List<Date> dateSuggestions = dateService.getDateSuggestions("test");
        assertFalse(dateSuggestions.isEmpty());
        assertEquals(dateSuggestions.get(0).getCity(), user.getCity());
    }

    @Test
    void shouldSwipeRightAndReturnStatusMatchedIfMatched() {
        when(matchRepository.findByUsername(match.getSwipedUsername())).thenReturn(Collections.singletonList(existingMatch));
        match.setMatch(true);
        when(matchRepository.save(match)).thenReturn(match);
        SwipeResponse swipeResponse = dateService.setLike(swipeDto);
        assertEquals(swipeResponse.getUsername(), swipeDto.getUsername());
        assertEquals(swipeResponse.getStatus(), "Match");
    }

    @Test
    void shouldSwipeRightAndSaveIfNotMatched() {
        when(matchRepository.findByUsername(match.getSwipedUsername())).thenReturn(Collections.emptyList());
        when(matchRepository.save(match)).thenReturn(match);
        SwipeResponse swipeResponse = dateService.setLike(swipeDto);
        assertEquals(swipeResponse.getUsername(), swipeDto.getUsername());
        assertEquals(swipeResponse.getStatus(), "Saved");
    }

    private UserProfile buildUserProfile() {
        UserPreferences userPreferences = UserPreferences.builder()
                .activePeopleStatus("")
                .genderChoice(GenderChoice.BOTH.toString())
                .politicalOpinion("").build();

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
                .gender(FEMALE.toString())
                .city("city")
                .aboutMe("Duh")
                .build();
    }
}
