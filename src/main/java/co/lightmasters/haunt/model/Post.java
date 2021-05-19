package co.lightmasters.haunt.model;

import co.lightmasters.haunt.model.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class Post {
    @NotBlank
    private String tweet;

    @NotNull
    private Date timeOfCreation;

    public static Post from(PostDto postDto) {
        return Post.builder()
                .tweet(postDto.getTweet())
                .timeOfCreation(new Date())
                .build();
    }
}
