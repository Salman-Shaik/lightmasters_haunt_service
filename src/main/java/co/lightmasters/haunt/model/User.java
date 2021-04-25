package co.lightmasters.haunt.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Builder
@Entity
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user", schema = "public")
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

    @Embedded
    @JsonUnwrapped
    @NotNull
    private UserProfile profile;
}
