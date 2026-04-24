package com.vromanyu.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vromanyu.event.OrderCreatedEvent;
import io.quarkus.logging.Log;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonPayloadConverter implements AttributeConverter<OrderCreatedEvent, String> {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(OrderCreatedEvent attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            Log.errorf("error while converting %s to json: %s", attribute, e.getMessage());
            return null;
        }
    }

    @Override
    public OrderCreatedEvent convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, OrderCreatedEvent.class);
        } catch (JsonProcessingException e) {
            Log.errorf("error while converting %s to OrderCreatedEvent: %s", dbData, e.getMessage());
            return null;
        }
    }
}
