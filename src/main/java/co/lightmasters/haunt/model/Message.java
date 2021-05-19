package co.lightmasters.haunt.model;

import co.lightmasters.haunt.model.dto.MessageDto;
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
@Table(name = "message", schema = "public")
public class Message {
    @Id
    @GeneratedValue
    private Long messageId;

    @NotNull
    private Long chatId;

    @NotBlank
    private String message;

    @NotBlank
    private String senderUsername;

    @NotNull
    private Date timeOfCreation;

    public static Message from(MessageDto messageDto) {
        return Message.builder()
                .message(messageDto.getMessage())
                .chatId(messageDto.getChatId())
                .senderUsername(messageDto.getSentUsername())
                .timeOfCreation(new Date())
                .build();
    }
}
