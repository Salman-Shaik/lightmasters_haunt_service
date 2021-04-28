package co.lightmasters.haunt.service;

import co.lightmasters.haunt.errors.InvalidCredentials;
import co.lightmasters.haunt.model.Credentials;
import co.lightmasters.haunt.model.Post;
import co.lightmasters.haunt.model.PostDto;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User saveUser(@Valid UserDto userDto) {
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = User.from(userDto, hashedPassword);
        return userRepository.save(user);
    }

    public Optional<User> fetchUser(String username) {
        return userRepository.findById(username);
    }

    public ResponseEntity<Object> loginUser(Credentials credentials) {
        Optional<User> optionalUser = fetchUser(credentials.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean validPassword = passwordEncoder.matches(credentials.getPassword(), user.getPassword());
            if (validPassword) {
                return ResponseEntity.ok("Login Success");
            }
            return invalidCredentialsResponse("Password", "Invalid Password");
        }
        return invalidCredentialsResponse("Username", "User Not Found");
    }

    private ResponseEntity<Object> invalidCredentialsResponse(String fieldName, String errorMessage) {
        InvalidCredentials invalidCredentialsMessage = InvalidCredentials.builder()
                .field(fieldName)
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(invalidCredentialsMessage);
    }
}
