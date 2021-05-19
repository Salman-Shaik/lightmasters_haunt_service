package co.lightmasters.haunt.model;

import co.lightmasters.haunt.model.dto.ChatDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
@Entity
@Table(name = "chat", schema = "public")
public class Chat {
    @Id
    @GeneratedValue
    private Long chatId;

    @NotNull
    private String username;

    @NotNull
    private String matchedUsername;

    public static Chat from(ChatDto chatDto) {
        return Chat.builder()
                .username(chatDto.getUsername())
                .matchedUsername(chatDto.getMatchedUsername())
                .build();
    }
}
