package co.lightmasters.haunt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
public class Date {
    @NotBlank
    private String name;

    @NotNull
    private Integer age;

    private String city;
    private String university;
    private String profession;
    private String aboutMe;
    private List<Prompt> prompts;
    public boolean drinking;
    public boolean smoking;
    public String sexuality;

    public static Date from(User user, UserProfile profile) {
        return Date.builder()
                .name(String.join(" ", user.getFirstName(), user.getLastName()))
                .age(user.getAge())
                .aboutMe(user.getAboutMe())
                .profession(profile.getProfession())
                .university(profile.getUniversity())
                .drinking(profile.isDrinking())
                .smoking(profile.isSmoking())
                .prompts(user.getPrompts())
                .sexuality(profile.getSexuality())
                .city(user.getCity())
                .build();
    }
}
