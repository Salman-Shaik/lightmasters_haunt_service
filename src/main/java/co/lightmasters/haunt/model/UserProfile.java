package co.lightmasters.haunt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import static co.lightmasters.haunt.model.GenderChoice.isBoth;


@Builder
@Entity
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile", schema = "public")
public class UserProfile {
    @Id
    @NotBlank
    private String username;

    private boolean student;
    private String university;
    private String profession;
    private String organization;
    public String education;
    public String height;
    public String sexuality;
    public String starSign;
    public String religion;
    public boolean drinking;
    public boolean smoking;

    @Embedded
    @JsonUnwrapped
    private UserPreferences userPreferences;

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    @JsonIgnore
    public String getGenderChoice() {
        return this.userPreferences.getGenderChoice();
    }

    @JsonIgnore
    public void setGenderChoice(String gender) {
        this.userPreferences.setGenderChoice(gender);
    }

    @JsonIgnore
    public boolean isPreferredGender(String gender) {
        return isBoth(this.getGenderChoice()) || this.getGenderChoice().equals(gender);
    }
}
