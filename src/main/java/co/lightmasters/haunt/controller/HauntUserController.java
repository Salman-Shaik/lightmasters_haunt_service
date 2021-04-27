package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.model.UserProfile;
import co.lightmasters.haunt.model.UserResponse;
import co.lightmasters.haunt.service.UserProfileService;
import co.lightmasters.haunt.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class HauntUserController {
    private final UserService userService;
    private final UserProfileService userProfileService;

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

}
