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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
@Embeddable
public class UserProfile {
    private boolean student;
    private String university;
    private String profession;
    private String organization;
    public String height;
    public String education;
    public String starSign;
    public String religion;
    public boolean drinking;
    public boolean smoking;

    @Embedded
    @JsonUnwrapped
    private Preferences preferences;
}
