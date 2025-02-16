package com.five.Maeum_Eum.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Converter
public class GenericListConverter<T> implements AttributeConverter<List<T>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}
