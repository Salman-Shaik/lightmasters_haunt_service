package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.repository.UserRepository;
import lombok.AllArgsConstructor;
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
        User user = User.from(userDto,hashedPassword);
        return userRepository.save(user);
    }

    public Optional<User> fetchUser(String username) {
        return userRepository.findById(username);
    }
}
