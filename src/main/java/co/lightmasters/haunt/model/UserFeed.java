package co.lightmasters.haunt.model;

import co.lightmasters.haunt.model.converters.PostConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
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
@Entity
@Table(name = "feed", schema = "public")
public class UserFeed {
    @Id
    @NotBlank
    private String username;

    @NotNull
    @Valid
    @Convert(converter = PostConverter.class)
    private List<Post> posts;

    public void addPost(Post post) {
        this.posts.add(post);
    }
}
