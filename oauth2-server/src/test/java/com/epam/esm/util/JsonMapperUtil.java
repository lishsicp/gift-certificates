package com.epam.esm.util;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

public class JsonMapperUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper =
            JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS).addModule(new JavaTimeModule()).build();
        objectMapper.setPropertyNamingStrategy(new SnakeCaseStrategy());
    }

    private JsonMapperUtil() {
        throw new UnsupportedOperationException();
    }

    public static String asJson(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T asObject(MvcResult mvcResult, Class<T> classType) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), classType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}