package com.wisetechglobal.exercise.automation.feature;

import com.wisetechglobal.exercise.automation.ScenarioContext;
import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.respository.AbstractBaseRepository;
import com.wisetechglobal.exercise.persistence.respository.DepartmentRepository;
import io.cucumber.java8.En;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static com.wisetechglobal.exercise.Utilities.*;
import static com.wisetechglobal.exercise.automation.ContextLoader.castObjectTo;
import static com.wisetechglobal.exercise.automation.RestAssuredUtil.execute;
import static org.junit.jupiter.api.Assertions.*;


public class DepartmentCrudStepDefs implements En {
    private static final String END_POINT = "/api/v1/department";

    private final ScenarioContext scenarioContext;

    private final AbstractBaseRepository<Department,Long> repository;

    public DepartmentCrudStepDefs(ScenarioContext sc, DepartmentRepository dr) {
        this.scenarioContext = sc;
        this.repository = dr;
        When("A request to create a department with name {}, required {}, read-only {} is made", (String name, Boolean required, Boolean readOnly) -> {
            Department department = Department.builder().name(name).required(required).readOnly(readOnly).build();
            BaseResponse<Department> response = execute(department, END_POINT, new HashMap<>(), getPostHeader(), BaseResponse.class, HttpMethod.POST);
            assertNotNull(response);
            scenarioContext.setDepartmentBaseResponse(response);
        });
        Then("A response is received with errorStatus {int} and message {} and data object isnull check returns {}", (Integer errorStatus, String message, Boolean dataNullCheck) -> {
            BaseResponse<Department> response = scenarioContext.getDepartmentBaseResponse();
            assertResponseStatus(response, errorStatus, message);
            if (dataNullCheck) assertNull(response.getData());
            else assertNotNull(response.getData());
        });
        And("A department is created in the database with values name: {}, required: {}, and read only: {}, given a success error status {int} is received in response", (String name, Boolean required, Boolean readOnly, Integer errorStatus) -> {
            if (errorStatus == 0) {
                long departmentId = Objects.requireNonNull(castObjectTo(scenarioContext.getDepartmentBaseResponse().getData(), Department.class)).getId();
                Optional<Department> departmentOptional = repository.findById(departmentId);
                assertTrue(departmentOptional.isPresent());
                Department departmentForDb = departmentOptional.get();
                assertDepartmentMatches(departmentForDb, name, required, readOnly);
            }
        });
        When("A request to fetch the department details with department id {long} is made", (Long departmentId) -> {
            BaseResponse<Department> response = execute(END_POINT + "/" + departmentId, new HashMap<>(), BaseResponse.class, HttpMethod.GET);
            assertNotNull(response);
            scenarioContext.setDepartmentBaseResponse(response);
        });

        And("The department id in the response matches {long}, given a success error status {int} is received in response", (Long departmentId, Integer errorStatus) -> {
            BaseResponse<Department> response = scenarioContext.getDepartmentBaseResponse();
            if (errorStatus == 0) {
                Department department = castObjectTo(response.getData(), Department.class);
                Long dbDepartmentId = Objects.requireNonNull(department).getId();
                assertEquals(departmentId, dbDepartmentId);
            }
        });
        When("A request is made to update department id {long} with name {}, required {} and read-only {}", (Long departmentId, String name, Boolean required, Boolean readOnly) -> {
            Department department = Department.builder().id(departmentId).name(name).required(required).readOnly(readOnly).build();
            BaseResponse<Department> response = execute(department, END_POINT, new HashMap<>(), getPostHeader(), BaseResponse.class, HttpMethod.PUT);
            assertNotNull(response);
            scenarioContext.setDepartmentBaseResponse(response);
        });
        And("The department with id {long} has updated value as name: {}, required: {} and readOnly: {} in the database, given a success error status {int} is received in response", (Long departmentId, String name, Boolean required, Boolean readOnly, Integer errorStatus) -> {
            if (errorStatus == 0) {
                Optional<Department> departmentOptional = repository.findById(departmentId);
                assertTrue(departmentOptional.isPresent());
                Department departmentForDb = departmentOptional.get();
                assertDepartmentMatches(departmentForDb, name, required, readOnly);
            }
        });
        When("A request is made to delete a department with id {long}",
                (Long departmentId) -> {
                    BaseResponse<Object> response = execute(END_POINT + "/" + departmentId, new HashMap<>(), BaseResponse.class, HttpMethod.DELETE);
                    assertNotNull(response);
                    scenarioContext.setOtherBaseResponse(response);
                });

        Then("A response with errorStatus {int} and message {} is received", (Integer errorStatus, String message) -> {
            BaseResponse<Object> response = scenarioContext.getOtherBaseResponse();
            assertResponseStatus(response, errorStatus, message);
        });

        And("The check for existence of department with id {long} in the database return {}", (Long departmentId, Boolean expectedDepartmentExist) -> {
            boolean actualDepartmentExist = repository.existsById(departmentId);
            assertEquals(expectedDepartmentExist, actualDepartmentExist);
        });
    }
}
