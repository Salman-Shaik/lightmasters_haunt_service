package co.lightmasters.haunt.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
public class ChatDto {
    @NotBlank
    private String username;

    @NotBlank
    private String matchedUsername;

    public static ChatDto from(SwipeDto swipeDto) {
        return ChatDto.builder()
                .username(swipeDto.getUsername())
                .matchedUsername(swipeDto.getSwipedUserName())
                .build();
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
