package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.UserDto;
import co.lightmasters.haunt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
class UserServiceTest {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .username("test")
                .password("password")
                .firstName("first")
                .lastName("last")
                .age(21)
                .gender("Male")
                .aboutMe("Duh")
                .build();
        user = User.from(userDto, "hashed");
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        userService = new UserService(passwordEncoder, userRepository);
    }

    @Test
    void shouldReturnHashedPasswordAfterSaving() {
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashed");
        when(userRepository.save(user)).thenReturn(user);

        User user = userService.saveUser(userDto);
        assertEquals(user.getPassword(), "hashed");
    }
}