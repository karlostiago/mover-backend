package com.ctsousa.mover.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMAT_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMAT_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        String value = parser.getText();

        try {
            return LocalDateTime.parse(value, FORMAT_BR);
        } catch (DateTimeParseException ignored) {}

        try {
            if (value.endsWith("Z")) {
                Instant instant = Instant.parse(value);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
            return LocalDateTime.parse(value, FORMAT_ISO);
        } catch (DateTimeParseException e) {
            throw new IOException("Data inválida: " + value);
        }
    }
}
