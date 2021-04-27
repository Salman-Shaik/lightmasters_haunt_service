package co.lightmasters.haunt.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Builder
@Entity
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class User {
    @Id
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;
    private String lastName;

    @Min(value = 18)
    @NotNull
    private Integer age;
    private String gender;

    private String aboutMe;

    public static User from(UserDto userDto,String password) {
        return User.builder()
                .aboutMe(userDto.getAboutMe())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .lastName(userDto.getLastName())
                .firstName(userDto.getFirstName())
                .username(userDto.getUsername())
                .password(password)
                .build();
    }
}
