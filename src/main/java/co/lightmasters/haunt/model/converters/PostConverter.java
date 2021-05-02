package co.lightmasters.haunt.model.converters;

import co.lightmasters.haunt.model.Post;
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
public class PostConverter implements AttributeConverter<List<Post>, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Post> posts) {
        String postsToJson = null;
        try {
            postsToJson = mapper.writeValueAsString(posts);
        } catch (JsonProcessingException e) {
            log.error("JSON writing error", e);
        }
        return postsToJson;
    }

    @Override
    public List<Post> convertToEntityAttribute(String postsAsJson) {

        List<Post> posts = Collections.emptyList();
        try {
            posts = mapper.readValue(postsAsJson, List.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }
        return posts;
    }

}
