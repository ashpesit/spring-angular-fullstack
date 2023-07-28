package com.wisetechglobal.exercise.unit.service;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.model.Employee;
import com.wisetechglobal.exercise.persistence.respository.EmployeeRepository;
import com.wisetechglobal.exercise.service.DepartmentService;
import com.wisetechglobal.exercise.service.EmployeeService;
import com.wisetechglobal.exercise.service.EmployeeServiceImpl;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;

import java.util.*;

import static com.wisetechglobal.exercise.Utilities.assertEmployeeMatches;
import static com.wisetechglobal.exercise.Utilities.assertResponseStatus;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest implements ConstantsAware {
    @Mock
    private DepartmentService departmentService;
    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService ref;
    private EmployeeServiceImpl concreteRef;

    private Department getRequiredDepartment(){
        return Department.builder().id(1L).name("dep1").required(true).readOnly(true).build();
    }

    private Employee getEmployee() {
        Set<Department> departmentSet = new HashSet<>();
        departmentSet.add(Department.builder().id(1L).name("dep2").required(true).readOnly(false).build());
        departmentSet.add(Department.builder().id(2L).name("dep3").required(false).readOnly(false).build());
        return Employee.builder().id(1L).firstName("fn").lastName("ln").emailAddress("fnln@email.com").departments(departmentSet).build();
    }

    @BeforeEach
    void init() {
        concreteRef = new EmployeeServiceImpl(departmentService, employeeRepository);
        ref = concreteRef;
    }

    @Test
    void createEmployee_DatabaseFailure_ThrowsDataAccessException() {
        Set<Long> departmentId = new HashSet<>(Arrays.asList(1L, 2L));
        doThrow(DataRetrievalFailureException.class).when(departmentService).departmentsExist(departmentId);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.createEmployee(getEmployee()));
    }

    @Test
    void createEmployee_DepartmentNotFound_ReturnsErrorBaseResponseWith404Status() {
        Set<Long> departmentId = new HashSet<>(Arrays.asList(1L, 2L));
        doReturn(false).when(departmentService).departmentsExist(departmentId);
        BaseResponse<Employee> response = ref.createEmployee(getEmployee());
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_DEPARTMENT_NOT_FOUND);
        assertNull(response.getData());
    }

    @Test
    void createEmployee_EmptyRequiredDepartment_ReturnsSuccessBaseResponseWith0Status() {
        Set<Long> departmentId = new HashSet<>(Arrays.asList(1L, 2L));
        doReturn(true).when(departmentService).departmentsExist(departmentId);
        Employee employee = getEmployee();
        doReturn(employee).when(employeeRepository).save(employee);
        doReturn(Collections.emptySet()).when(departmentService).getRequiredDepartments();
        BaseResponse<Employee> response = ref.createEmployee(employee);
        assertResponseStatus(response, SUCCESS_CODE, SUCCESS_MESSAGE);
        assertEquals(employee, response.getData());
    }

    @Test
    void createEmployee_RequiredDepartmentsAvailable_ReturnsSuccessBaseResponseWith0Status() {
        Set<Long> departmentId = new HashSet<>(Arrays.asList(1L, 2L));
        doReturn(true).when(departmentService).departmentsExist(departmentId);
        Employee employee = getEmployee();
        doReturn(employee).when(employeeRepository).save(employee);
        doReturn(new HashSet<>(Collections.singleton(getRequiredDepartment()))).when(departmentService).getRequiredDepartments();
        BaseResponse<Employee> response = ref.createEmployee(employee);
        assertResponseStatus(response, SUCCESS_CODE, SUCCESS_MESSAGE);
        assertEquals(employee, response.getData());
    }

    @Test
    void getEmployee_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(employeeRepository).findById(1L);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.getEmployee(1L));
    }

    @Test
    void getEmployee_EmployeeNotFound_ReturnsErrorBaseResponseWith404Status() {
        doReturn(Optional.empty()).when(employeeRepository).findById(1L);
        BaseResponse<Employee> response = ref.getEmployee(1L);
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_EMPLOYEE_NOT_FOUND);
        assertNull(response.getData());
    }

    @Test
    void getEmployee_EmployeeFound_ReturnsSuccessBaseResponseWith0Status() {
        doReturn(Optional.of(getEmployee())).when(employeeRepository).findById(1L);
        BaseResponse<Employee> response = ref.getEmployee(1L);
        assertResponseStatus(response, SUCCESS_CODE, SUCCESS_MESSAGE);
        assertEmployeeMatches(getEmployee(), response.getData());
    }


    @Test
    void updateEmployee_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(employeeRepository).existsById(1L);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.updateEmployee(getEmployee()));
    }

    @Test
    void updateEmployee_EmployeeNotFound_ReturnsErrorBaseResponseWith404Status() {
        doReturn(false).when(employeeRepository).existsById(1L);
        BaseResponse<Employee> response = ref.updateEmployee(getEmployee());
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_EMPLOYEE_NOT_FOUND);
        assertNull(response.getData());
    }

    @Test
    void updateEmployee_DepartmentNotFound_ReturnsErrorBaseResponseWith404Status() {
        doReturn(true).when(employeeRepository).existsById(1L);
        Set<Long> departmentId = new HashSet<>(Arrays.asList(1L, 2L));
        doReturn(false).when(departmentService).departmentsExist(departmentId);
        BaseResponse<Employee> response = ref.updateEmployee(getEmployee());
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_DEPARTMENT_NOT_FOUND);
        assertNull(response.getData());
    }


    @Test
    void updateEmployee_EmployeeAndDepartmentFound_ReturnsSuccessBaseResponseWith0Status() {
        doReturn(true).when(employeeRepository).existsById(1L);
        Set<Long> departmentId = new HashSet<>(Arrays.asList(1L, 2L));
        doReturn(true).when(departmentService).departmentsExist(departmentId);
        Employee employee = getEmployee();
        doReturn(employee).when(employeeRepository).save(employee);
        doReturn(Collections.singleton(1L)).when(departmentService).getRequiredDepartments();
        BaseResponse<Employee> response = ref.updateEmployee(employee);
        assertResponseStatus(response, SUCCESS_CODE, SUCCESS_MESSAGE);
        assertEquals(employee, response.getData());
    }


    @Test
    void deleteEmployee_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(employeeRepository).existsById(1L);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.deleteEmployee(1L));
    }

    @Test
    void deleteEmployee_EmployeeNotFound_ReturnsErrorBaseResponseWith0Status() {
        doReturn(false).when(employeeRepository).existsById(1L);
        BaseResponse<Object> response = ref.deleteEmployee(1L);
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_EMPLOYEE_NOT_FOUND);
    }

    @Test
    void deleteEmployee_DeleteSuccessful_ReturnsSuccessBaseResponseWith0Status() {
        doReturn(true).when(employeeRepository).existsById(1L);
        doNothing().when(employeeRepository).deleteById(1L);
        BaseResponse<Object> response = ref.deleteEmployee(1L);
        assertResponseStatus(response, SUCCESS_CODE, SUCCESS_MESSAGE);
    }

}
