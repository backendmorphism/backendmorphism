package com.backendmorphism.backend.ai.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class AiJsonParser {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public JsonNode parseJson(String rawResponse) {

        try {

            // extract JSON block
            int start = rawResponse.indexOf("{");
            int end = rawResponse.lastIndexOf("}");

            if (start == -1 || end == -1) {
                throw new RuntimeException(
                        "No valid JSON found"
                );
            }

            String cleanJson =
                    rawResponse.substring(start, end + 1);

            return objectMapper.readTree(cleanJson);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to parse AI JSON response"
            );
        }
    }
}