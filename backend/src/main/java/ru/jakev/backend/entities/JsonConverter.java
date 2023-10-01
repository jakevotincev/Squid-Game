package ru.jakev.backend.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Converter
public class JsonConverter implements AttributeConverter<Map<String, Boolean>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger LOG = LoggerFactory.getLogger(JsonConverter.class);
    private final TypeReference<Map<String, Boolean>> mapTypeRef = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(Map<String, Boolean> data) {
        if (null == data) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            LOG.error("Couldn't convert data-map to JSON String.", ex);
            return null;
        }
    }

    @Override
    public Map<String, Boolean> convertToEntityAttribute(String dbData) {
        if (null == dbData || dbData.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(dbData, mapTypeRef);
        } catch (IOException ex) {
            LOG.error("Couldn't convert JSON String to data-map.", ex);
            return null;
        }
    }
}
