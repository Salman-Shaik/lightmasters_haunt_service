package co.lightmasters.haunt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
@Embeddable
public class UserPreferences {
    public String genderChoice;
    public String activePeopleStatus;
    public String politicalOpinion;
}
