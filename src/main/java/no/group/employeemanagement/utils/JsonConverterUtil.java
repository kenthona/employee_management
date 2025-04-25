package no.group.employeemanagement.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author austraramadhan
 *         Experimental
 */
public class JsonConverterUtil {

    private JsonConverterUtil() {}

    private static final Logger log = LoggerFactory.getLogger(JsonConverterUtil.class);

    /**
     * Converts a list of objects into a list of objects of the specified class type using JSON serialization and deserialization.
     * <p>
     * This method serializes each object in the input list into a JSON string representation and then deserializes it back into
     * an object of the specified class type using Jackson ObjectMapper. The resulting list contains objects of the specified class type.
     *
     * @param list The list of objects to be converted.
     * @param clazz The class type of the objects to be returned.
     * @param <T> The type of the objects to be returned.
     * @return A list of objects of type T parsed from the input list, or an empty list if the input list is null.
     */
    public static <T> List<T> fromlist(List<?> list, final Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // fixing java.time.LocalDate not supported by default
        // ObjectMapper 20230902
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<T> ret = new ArrayList<>();
        for (Object item : list) {
            String itemString = objectToJsonString(objectMapper, item);
            T dto = jsonStringToObject(objectMapper, itemString, clazz);
            ret.add(dto);
        }
        return ret;
    }

    /**
     * Converts an object into an object of the specified class type using JSON serialization and deserialization.
     * <p>
     * This method serializes the input object into a JSON string representation and then deserializes it back into
     * an object of the specified class type using Jackson ObjectMapper.
     *
     * @param object The object to be converted.
     * @param clazz The class type of the object to be returned.
     * @param <T> The type of the object to be returned.
     * @return An object of type T parsed from the input object, or null if the input object is null or cannot be parsed.
     */
    public static <T> T fromObject(final Object object, final Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // fixing java.time.LocalDate not supported by default
        // ObjectMapper 20230530
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String itemString = objectToJsonString(objectMapper, object);
        return jsonStringToObject(objectMapper, itemString, clazz);
    }

    public static String fromObjectToString(final Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectToJsonString(objectMapper, object);
    }

    /**
     * Converts a JSON string into an object of the specified class type using JSON deserialization.
     * <p>
     * This method deserializes the input JSON string into an object of the specified class type using Jackson ObjectMapper.
     *
     * @param object The JSON string to be converted.
     * @param clazz The class type of the object to be returned.
     * @param <T> The type of the object to be returned.
     * @return An object of type T parsed from the JSON string, or null if the JSON string is null or cannot be parsed.
     */
    public static <T> T fromJsonString(final String object, final Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // fixing java.time.LocalDate not supported by default
        // ObjectMapper 20230530
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return jsonStringToObject(objectMapper, object, clazz);
    }

    public static <T> T fromJsonString(final String object, final TypeReference<T> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(object, typeReference);
        } catch (IOException e) {
            log.error("Error IOException fromJsonString : {}", e.toString(), e);
        }

        return null;
    }

    public static <T> List<T> fromListJsonString(final String object, final Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<T> ret = new ArrayList<>();
        try {
            List<T> list = objectMapper.readValue(object, new TypeReference<>() {
            });
            for (T item : list) {
                String itemString = objectToJsonString(objectMapper, item);
                T dto = jsonStringToObject(objectMapper, itemString, clazz);
                ret.add(dto);
            }
            return ret;
        } catch (IOException e) {
            log.error("Error IOException fromListJsonString : {}", e.toString(), e);
        }

        return Collections.emptyList();
    }

    private static <T> String objectToJsonString(ObjectMapper objectMapper, T item) {
        String itemString = null;
        try {
            itemString = objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("Error json processing : {}", e.toString(), e);
        }

        return itemString;
    }

    private static <T> T jsonStringToObject(ObjectMapper objectMapper, String itemString, Class<T> clazz) {
        T dto = null;
        try {
            dto = objectMapper.readValue(itemString, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error json processing : {}", e.toString(), e);
        }

        return dto;
    }
}
