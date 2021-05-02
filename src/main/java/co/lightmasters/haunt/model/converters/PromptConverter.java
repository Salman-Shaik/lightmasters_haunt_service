package co.lightmasters.haunt.model.converters;

import co.lightmasters.haunt.model.Prompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Converter
public class PromptConverter implements AttributeConverter<List<Prompt>, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Prompt> posts) {
        String promptsToJson = null;
        try {
            promptsToJson = mapper.writeValueAsString(posts);
        } catch (JsonProcessingException e) {
            log.error("JSON writing error", e);
        }
        return promptsToJson;
    }

    @Override
    public List<Prompt> convertToEntityAttribute(String postsAsJson) {

        List<Prompt> prompts = Collections.emptyList();
        try {
            prompts = mapper.readValue(postsAsJson, List.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }
        return prompts;
    }

}
