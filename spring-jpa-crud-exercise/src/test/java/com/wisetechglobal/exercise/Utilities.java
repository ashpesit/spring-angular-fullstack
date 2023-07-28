package com.wisetechglobal.exercise;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.model.Employee;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class Utilities {
    public static void assertResponseStatus(BaseResponse<?> response, int expected, String expectedMsg) {
        assertNotNull(response);
        assertEquals(expectedMsg, response.getMessage());
        assertEquals(expected, response.getErrorStatus());
    }

    public static Map<String, String> getPostHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public static void assertDepartmentMatches(Department expected, Department actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getRequired(), actual.getRequired());
        assertEquals(expected.getReadOnly(), actual.getReadOnly());
    }

    public static void assertDepartmentMatches(Department departmentForDb, String name, Boolean required, Boolean readOnly) {
        assertEquals(name, departmentForDb.getName());
        assertEquals(required, departmentForDb.getRequired());
        assertEquals(readOnly, departmentForDb.getReadOnly());
    }

    public static void assertEmployeeMatches(Employee employeeFromDb, String firstName, String lastName, String emailAddress, String departmentIds) {
        assertEquals(firstName, employeeFromDb.getFirstName());
        assertEquals(lastName, employeeFromDb.getLastName());
        assertEquals(emailAddress, employeeFromDb.getEmailAddress());
        Set<Long> departmentIdSet = StringUtils.isNotBlank(departmentIds) ? Arrays.stream(departmentIds.split(",")).map(Long::parseLong).collect(Collectors.toSet()) : Collections.emptySet();
        employeeFromDb.getDepartments().forEach(d -> assertTrue(d.getRequired() || departmentIdSet.contains(d.getId())));
    }

    public static void assertEmployeeMatches(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmailAddress(), actual.getEmailAddress());
    }
}
