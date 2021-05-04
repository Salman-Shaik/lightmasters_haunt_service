package co.lightmasters.haunt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
public class UserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String aboutMe;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .aboutMe(user.getAboutMe())
                .gender(user.getGender())
                .age(user.getAge())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .build();
    }
}
