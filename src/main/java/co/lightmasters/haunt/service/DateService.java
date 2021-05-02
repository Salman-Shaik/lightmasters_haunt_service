package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Date;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.repository.UserProfileRepository;
import co.lightmasters.haunt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DateService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

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
        List<User> allUserByCity = userRepository.findAllByCity(userCity);
        return allUserByCity.stream()
                .map(this::getDateSuggestionFromUserCity)
                .collect(Collectors.toList());
    }

    private Date getDateSuggestionFromUserCity(User user) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(user.getUsername());
        if (userProfile.isPresent()) {
            UserProfile profile = userProfile.get();
            return Date.from(user, profile);
        }
        return null;
    }
}
