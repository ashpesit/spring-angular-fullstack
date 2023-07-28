package com.wisetechglobal.exercise.automation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetechglobal.exercise.ExerciseApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import javax.annotation.PostConstruct;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(classes = ExerciseApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ContextLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PROFILE_FILE_LOCATION = "./src/test/resources/json";

    @PostConstruct
    public void restAssuredSetup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    public static <T> T castObjectTo(Object ob, Class<T> clazz) {
        try {
            String str = objectMapper.writeValueAsString(ob);
            return objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.error("un-parsable file: {}", e.getMessage());
        }
        return null;
    }

    public static <T> T castObjectToArray(Object ob, TypeReference<T> clazz) {
        try {
            String str = objectMapper.writeValueAsString(ob);
            return objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.error("un-parsable file: {}", e.getMessage());
        }
        return null;
    }
}
