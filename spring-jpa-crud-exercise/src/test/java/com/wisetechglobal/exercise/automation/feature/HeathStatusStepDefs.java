package com.wisetechglobal.exercise.automation.feature;

import com.wisetechglobal.exercise.automation.ScenarioContext;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static io.restassured.RestAssured.given;


@RequiredArgsConstructor
public class HeathStatusStepDefs {
    private static final String END_POINT = "/api/actuator/health";
    @Given("The system is in healthy state")
    public void theSystemIsInHealthyState() {
        given().when().get(END_POINT).then().statusCode(200);
    }
}
