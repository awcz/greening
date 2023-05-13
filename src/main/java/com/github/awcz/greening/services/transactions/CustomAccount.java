package com.github.awcz.greening.services.transactions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.awcz.greening.generated.transactions.Account;
import io.micronaut.core.annotation.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A custom class extending the Account model to generate the expected
 * response format (two decimal places for the account balance)
 */
class CustomAccount extends Account {

    @Nullable
    @JsonProperty(JSON_PROPERTY_BALANCE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    @JsonSerialize(using = FloatSerializer.class)
    @Override
    public Float getBalance() {
        return super.getBalance();
    }

    private static class FloatSerializer extends StdSerializer<Float> {

        FloatSerializer() {
            this(null);
        }

        FloatSerializer(Class<Float> t) {
            super(t);
        }

        @Override
        public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeNumber(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP));
        }
    }
}
