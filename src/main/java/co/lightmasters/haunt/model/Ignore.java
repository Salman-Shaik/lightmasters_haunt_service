package co.lightmasters.haunt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
@Entity
@Table(name = "ignored", schema = "public")
public class Ignore {
    @Id
    @Generated
    private Integer id;

    @NotBlank
    private String username;

    @NotBlank
    private String IgnoredUsername;

    @NotNull
    private Date lastModified;

    public static Ignore from(SwipeDto swipeDto) {
        return Ignore.builder()
                .username(swipeDto.getUsername())
                .IgnoredUsername(swipeDto.getSwipedUserName())
                .lastModified(new Date())
                .build();
    }
}
