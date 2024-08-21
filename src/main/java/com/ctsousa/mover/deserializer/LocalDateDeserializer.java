package com.ctsousa.mover.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter BR_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        String value = parser.getText();
        if (value.contains("T")) {
            value = value.split("T")[0];  // Ignorar a parte de tempo
        }
        try {
            return LocalDate.parse(value, ISO_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(value, BR_FORMATTER);
        }
    }
}
