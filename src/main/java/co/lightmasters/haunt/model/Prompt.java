package co.lightmasters.haunt.model;

import co.lightmasters.haunt.model.dto.PromptDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
public class Prompt {
    private String question;
    private String answer;

    public static Prompt from(PromptDto promptDto) {
        return Prompt.builder().question(promptDto.getQuestion()).answer(promptDto.getAnswer()).build();
    }
}
