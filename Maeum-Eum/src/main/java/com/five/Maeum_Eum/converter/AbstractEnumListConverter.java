package com.five.Maeum_Eum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.stream.Collectors;

@Converter
public abstract class AbstractEnumListConverter<T extends Enum<T>> implements AttributeConverter<List<T>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> enumType;

    protected AbstractEnumListConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

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
            List<String> values = objectMapper.readValue(dbData, new TypeReference<>() {});
            return values.stream()
                    .map(value -> Enum.valueOf(enumType, value))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}
