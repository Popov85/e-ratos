package ua.edu.ratos.dao.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashSet;
import java.util.Set;

@Converter
public class JsonToSetConverter implements AttributeConverter<Set<String>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Log and return null or empty string as per your error handling policy
            return null;
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<HashSet<String>>() {});
        } catch (Exception e) {
            // Proper error handling or logging should be done here
            return new HashSet<>();
        }
    }
}
