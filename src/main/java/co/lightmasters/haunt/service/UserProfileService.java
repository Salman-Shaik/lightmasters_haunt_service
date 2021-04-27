package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserProfileService {
    public final UserProfileRepository userProfileRepository;

    public UserProfile saveProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public Optional<UserProfile> fetchProfile(String username) {
        return userProfileRepository.findById(username);
    }
}
