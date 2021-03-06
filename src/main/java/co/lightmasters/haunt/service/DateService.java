package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Chat;
import co.lightmasters.haunt.model.dto.ChatDto;
import co.lightmasters.haunt.model.Date;
import co.lightmasters.haunt.model.Ignore;
import co.lightmasters.haunt.model.Match;
import co.lightmasters.haunt.model.dto.SwipeDto;
import co.lightmasters.haunt.model.SwipeResponse;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.repository.IgnoreRepository;
import co.lightmasters.haunt.repository.MatchRepository;
import co.lightmasters.haunt.repository.UserProfileRepository;
import co.lightmasters.haunt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.lightmasters.haunt.model.GenderChoice.isBoth;

@Component
@AllArgsConstructor
public class DateService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MatchRepository matchRepository;
    private final IgnoreRepository ignoreRepository;
    private final ChatService chatService;

    public List<Date> getDateSuggestions(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        List<Date> dateSuggestions = Collections.emptyList();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            dateSuggestions = getDateSuggestionForUser(user);
        }
        return dateSuggestions.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Date> getDateSuggestionForUser(User user) {
        String userCity = user.getCity();
        Optional<UserProfile> userProfile = userProfileRepository.findById(user.getUsername());
        if (userProfile.isPresent()) {
            String genderChoice = userProfile.get().getGenderChoice();
            List<User> allUserByCity;
            if (isBoth(genderChoice)) {
                allUserByCity = userRepository.findByCity(userCity);
            } else {
                allUserByCity = userRepository.findByCityAndGender(userCity, genderChoice);
            }
            return allUserByCity.stream()
                    .map(profile -> getDateSuggestionFromUserCity(profile, user.getGender()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Date getDateSuggestionFromUserCity(User user, String gender) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(user.getUsername());
        if (userProfile.isPresent()) {
            UserProfile profile = userProfile.get();
            if (profile.isPreferredGender(gender)) {
                return Date.from(user, profile);
            }
            return null;
        }
        return null;
    }

    public SwipeResponse setLike(SwipeDto swipeDto) {
        Match match = Match.from(swipeDto);
        Long chatId = null;
        List<Match> allMatches = matchRepository.findByUsername(match.getSwipedUsername());
        List<Match> registeredSwipes = allMatches.stream()
                .filter(info -> info.getSwipedUsername().equals(match.getUsername()))
                .collect(Collectors.toList());
        if (registeredSwipes.size() == 1) {
            chatId = createMatchAndChat(swipeDto, match, registeredSwipes);
        }
        Match save = matchRepository.save(match);
        return SwipeResponse.builder()
                .username(save.getUsername())
                .status(!registeredSwipes.isEmpty() ? "Match" : "Saved")
                .chatId(chatId)
                .build();
    }

    private Long createMatchAndChat(SwipeDto swipeDto, Match match, List<Match> registeredSwipes) {
        Match registeredSwipe = registeredSwipes.get(0);
        match.setMatch(true);
        registeredSwipe.setMatch(true);
        matchRepository.save(registeredSwipe);
        ChatDto chatDto = ChatDto.from(swipeDto);
        Chat chat = chatService.createChat(chatDto);
        return chat.getChatId();
    }

    public void setIgnore(SwipeDto swipeDto) {
        Ignore ignoreUser = Ignore.from(swipeDto);
        ignoreRepository.save(ignoreUser);
    }

    public SwipeResponse removeLike(SwipeDto swipeDto) {
        Match match = Match.from(swipeDto);
        matchRepository.deleteByUsernameAndSwipedUsername(match.getUsername(), match.getSwipedUsername());
        chatService.deleteChat(match.getUsername(),match.getSwipedUsername());
        setIgnore(swipeDto);
        return SwipeResponse.builder()
                .username(match.getUsername())
                .status("Deleted")
                .build();
    }
}
