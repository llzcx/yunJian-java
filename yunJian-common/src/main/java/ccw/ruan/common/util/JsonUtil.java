package ccw.ruan.common.util;

import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.request.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();


    public static String object2StringSlice(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


    public static <T> T deserialize(String json, Class<T> valueType)  {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new SystemException(ResultCode.JSON_ERROR);
        }
    }

}