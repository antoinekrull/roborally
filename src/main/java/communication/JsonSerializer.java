package communication;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Antoine
 * @version 1.0
 */
public class JsonSerializer {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String serializeJson(T input){
        try{
            return objectMapper.writeValueAsString(input);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeJson(String input, Class<T> messageClass){
        try{
            return objectMapper.readValue(input, messageClass);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}

