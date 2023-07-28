package com.wisetechglobal.exercise.automation.feature;

import com.wisetechglobal.exercise.automation.ScenarioContext;
import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.model.Employee;
import com.wisetechglobal.exercise.persistence.respository.AbstractBaseRepository;
import com.wisetechglobal.exercise.persistence.respository.EmployeeRepository;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.stream.Collectors;

import static com.wisetechglobal.exercise.Utilities.*;
import static com.wisetechglobal.exercise.automation.ContextLoader.castObjectTo;
import static com.wisetechglobal.exercise.automation.RestAssuredUtil.execute;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EmployeeCrudStepDefs implements En, ConstantsAware {
    private static final String END_POINT = "/api/v1/employee";

    private final ScenarioContext scenarioContext;
    private final AbstractBaseRepository<Employee,Long> repository;

    public EmployeeCrudStepDefs(ScenarioContext sc, EmployeeRepository er) {
        this.scenarioContext = sc;
        this.repository = er;
        When("A request to create an employee with first name {}, last name {}, email address {} and department ids {}", (String firstName, String lastName, String emailAddress, String departmentIds) -> {
            Set<Department> departmentSet = StringUtils.isNotBlank(departmentIds) ? Arrays.stream(departmentIds.split(",")).map(d -> Department.builder().id(Long.parseLong(d)).build()).collect(Collectors.toSet()) : Collections.emptySet();
            Employee employee = Employee.builder().firstName(firstName).lastName(lastName).emailAddress(emailAddress).departments(departmentSet).build();
            BaseResponse<Employee> response = execute(employee, END_POINT, new HashMap<>(), getPostHeader(), BaseResponse.class, HttpMethod.POST);
            assertNotNull(response);
            scenarioContext.setEmployeeBaseResponse(response);
        });
        Then("A response with error status is: {int}, message contains: {} is received and null check on data value is: {}", (Integer errorStatus, String message, String nullCheckOnData) -> {
            boolean nullCheckOnDataBoolean = Boolean.parseBoolean(nullCheckOnData);
            BaseResponse<Employee> response = scenarioContext.getEmployeeBaseResponse();
            assertResponseStatus(response, errorStatus, message);
            if (nullCheckOnDataBoolean) assertNull(response.getData());
            else assertNotNull(response.getData());
        });
        And("An employee is created in the database with values first name: {}, last name: {}, email address: {} and departments {}, given a success error status {int} is received in response", (String firstName, String lastName, String emailAddress, String departmentIds, Integer errorStatus) -> {
            if (errorStatus == 0) {
                Long employeeId = Objects.requireNonNull(castObjectTo(scenarioContext.getEmployeeBaseResponse().getData(), Employee.class)).getId();
                Optional<Employee> employeeOptional = repository.findById(employeeId);
                assertTrue(employeeOptional.isPresent());
                Employee employeeFromDb = employeeOptional.get();
                assertEmployeeMatches(employeeFromDb, firstName, lastName, emailAddress, departmentIds);
            }
        });

        When("A request is made to fetch employee details with employee id {int}", (Integer employeeId) -> {
            BaseResponse<Employee> response = execute(END_POINT + "/" + employeeId, new HashMap<>(), BaseResponse.class, HttpMethod.GET);
            assertNotNull(response);
            scenarioContext.setEmployeeBaseResponse(response);
        });

        And("The employee id in the response matches {long}, given a success error status {int} is received in response", (Long employeeId, Integer errorStatus) -> {
            if (errorStatus == 0) {
                Employee employee = castObjectTo(scenarioContext.getEmployeeBaseResponse().getData(), Employee.class);
                assertNotNull(employee);
                assertEquals(employeeId, employee.getId());
            }
        });

        When("A request is made to update employee id: {long} with first name: {}, last name: {}, email address: {} and department ids: {}", (Long employeeId, String firstName, String lastName, String emailAddress, String departmentIds) -> {
            log.info(Arrays.toString(departmentIds.split(",")));
            Set<Department> departmentSet = StringUtils.isNotBlank(departmentIds) ? Arrays.stream(departmentIds.split(",")).map(d -> Department.builder().id(Long.parseLong(d)).build()).collect(Collectors.toSet()) : Collections.emptySet();
            Employee employee = Employee.builder().id(employeeId).firstName(firstName).lastName(lastName).emailAddress(emailAddress).departments(departmentSet).build();
            BaseResponse<Employee> response = execute(employee, END_POINT, new HashMap<>(), getPostHeader(), BaseResponse.class, HttpMethod.PUT);
            assertNotNull(response);
            scenarioContext.setEmployeeBaseResponse(response);
        });

        And("The employee with employee id: {long} in the database is updated with values first name: {}, last name: {}, email address: {} and departments {}, given a success error status {int} is received in response", (Long employeeId, String firstName, String lastName, String emailAddress, String departmentIds, Integer errorStatus) -> {
            if (errorStatus == 0) {
                Optional<Employee> employeeOptional = repository.findById((long) employeeId);
                assertTrue(employeeOptional.isPresent());
                Employee employeeFromDb = employeeOptional.get();
                assertEmployeeMatches(employeeFromDb, firstName, lastName, emailAddress, departmentIds);
            }
        });

        When("A request to delete an employee with id {long} is made", (Long employeeId) -> {
            BaseResponse<Employee> response = execute(END_POINT + "/" + employeeId, new HashMap<>(), BaseResponse.class, HttpMethod.DELETE);
            assertNotNull(response);
            scenarioContext.setEmployeeBaseResponse(response);
        });

        Then("A response with error status {int}, message {} is received", (Integer errorStatus, String message) -> {
            BaseResponse<Employee> response = scenarioContext.getEmployeeBaseResponse();
            assertResponseStatus(response, errorStatus, message);
        });

        And("Existence check of employee with id {int} in the database returns {}", (Integer employeeId, String exist) -> {
            Boolean existBoolean = Boolean.parseBoolean(exist);
            assertEquals(existBoolean, repository.existsById((long) employeeId));
        });

    }
}
