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


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
@Entity
@Table(name = "swipes", schema = "public")
public class Swipe {
    @Id
    @Generated
    private Integer id;
    @NotBlank
    private String username;

    @NotBlank
    private String swipedUsername;

    @NotNull
    private boolean isMatch;

    public static Swipe from(SwipeDto swipeDto) {
        return Swipe.builder()
                .username(swipeDto.getUsername())
                .swipedUsername(swipeDto.getSwipedUserName())
                .isMatch(false)
                .build();
    }
}
