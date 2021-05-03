package co.lightmasters.haunt.model;

import co.lightmasters.haunt.model.converters.PromptConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;


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

    @NotBlank
    private String city;
    private String aboutMe;

    @Convert(converter = PromptConverter.class)
    private List<Prompt> prompts;

    public static User from(UserDto userDto,String password) {
        return User.builder()
                .aboutMe(userDto.getAboutMe())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .lastName(userDto.getLastName())
                .firstName(userDto.getFirstName())
                .username(userDto.getUsername())
                .password(password)
                .prompts(Collections.emptyList())
                .city("CITY")
                .build();
    }

    public void addPrompt(Prompt prompt) {
        this.prompts.add(prompt);
    }
}
