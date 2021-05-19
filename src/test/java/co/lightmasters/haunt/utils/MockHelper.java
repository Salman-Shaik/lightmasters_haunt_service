package co.lightmasters.haunt.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockHelper {
    public static Object mapJsonToClass(String json, Object desiredClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, desiredClass.getClass());
    }
}
