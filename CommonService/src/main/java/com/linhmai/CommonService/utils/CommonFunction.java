package com.linhmai.CommonService.utils;

import com.linhmai.CommonService.common.ValidateException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@NoArgsConstructor
public class CommonFunction {

    @SneakyThrows
    public static void jsonValidate(InputStream inputStream, String json) {
        if (inputStream == null) {
            log.error("Schema InputStream is null. Check the schema path.");
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("schema", "Schema not found");
            throw new ValidateException("RQ01", errorMap, HttpStatus.BAD_REQUEST);
        }

        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(inputStream);
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(json);
        Set<ValidationMessage> errors = schema.validate(jsonNode);
        log.error("Validation errors: {}", errors);

        if (!errors.isEmpty()) {
            StringBuilder combinedErrors = new StringBuilder();
            for (ValidationMessage error : errors) {
                combinedErrors.append(error.getMessage()).append(", ");
            }
            // Loại bỏ dấu phẩy cuối cùng
            String errorMessage = combinedErrors.substring(0, combinedErrors.length() - 2);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("$", errorMessage);
            throw new ValidateException("RQ01", errorMap, HttpStatus.BAD_REQUEST);
        }
    }
}
