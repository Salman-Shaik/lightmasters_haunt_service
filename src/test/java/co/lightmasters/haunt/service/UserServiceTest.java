package co.lightmasters.haunt.service;

import co.lightmasters.haunt.errors.InvalidCredentials;
import co.lightmasters.haunt.model.Credentials;
import co.lightmasters.haunt.model.GenderChoice;
import co.lightmasters.haunt.model.User;
import co.lightmasters.haunt.model.dto.UserDto;
import co.lightmasters.haunt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
class UserServiceTest {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserService userService;

    private User user;
    private UserDto userDto = UserDto.builder()
            .username("test")
            .password("password")
            .firstName("first")
            .lastName("last")
            .age(21)
            .gender(GenderChoice.MALE.toString())
            .aboutMe("Duh")
            .build();
    private Credentials credentials;

    @BeforeEach
    void setUp() {
        user = User.from(userDto, "hashed");
        credentials = Credentials.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
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

    @Test
    void shouldLoginUserWhenValidCredentialsAreProvided() {
        when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(true);
        when(userRepository.findById(user.getUsername())).thenReturn(Optional.ofNullable(user));

        ResponseEntity<Object> responseEntity = userService.loginUser(credentials);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), "Login Success");
    }

    @Test
    void shouldFailLoginWhenInvalidUsernameIsProvided() {
        ResponseEntity<Object> responseEntity = userService.loginUser(credentials);
        InvalidCredentials invalidCredentials = InvalidCredentials.builder().field("Username").message("User Not Found").build();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(responseEntity.getBody(), invalidCredentials);
    }

    @Test
    void shouldFailLoginWhenInvalidPasswordIsProvided() {
        when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(false);
        when(userRepository.findById(user.getUsername())).thenReturn(Optional.ofNullable(user));

        ResponseEntity<Object> responseEntity = userService.loginUser(credentials);
        InvalidCredentials invalidCredentials = InvalidCredentials.builder().field("Password").message("Invalid Password").build();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(responseEntity.getBody(), invalidCredentials);
    }
}
