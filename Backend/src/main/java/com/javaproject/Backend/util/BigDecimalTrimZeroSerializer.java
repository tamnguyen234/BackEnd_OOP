package com.javaproject.Backend.util;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BigDecimalTrimZeroSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.stripTrailingZeros().scale() <= 0) {
            // Nếu không có phần thập phân -> in ra int
            gen.writeNumber(value.longValue());
        } else {
            gen.writeNumber(value);
        }
    }
}
