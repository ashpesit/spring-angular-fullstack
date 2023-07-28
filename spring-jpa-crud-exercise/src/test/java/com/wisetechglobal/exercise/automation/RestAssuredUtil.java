package com.wisetechglobal.exercise.automation;

import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredUtil {
    public static <T> T execute(Object body, String endPoint, Map<String, String> params, Map<String, String> header, Class<T> clazz, HttpMethod method) {
        RequestSpecification builder = given().log().all();
        for (Map.Entry<String, String> entry : header.entrySet())
            builder = builder.header(entry.getKey(), entry.getValue());
        for (Map.Entry<String, String> entry : params.entrySet())
            builder = builder.params(entry.getKey(), entry.getValue());
        return switch (method) {
            case POST -> builder.body(body).when().post(endPoint).as(clazz);
            case GET -> builder.when().get(endPoint).as(clazz);
            case PATCH -> builder.body(body).when().patch(endPoint).as(clazz);
            case PUT ->
                    body == null ? builder.when().put(endPoint).as(clazz) : builder.body(body).when().put(endPoint).as(clazz);
            case DELETE -> builder.when().delete(endPoint).as(clazz);
            default -> null;
        };
    }

    public static <T> T execute(String endPoint, Map<String, String> params, Class<T> clazz, HttpMethod method) {
        return execute(null, endPoint, params, new HashMap<>(), clazz, method);
    }
}
