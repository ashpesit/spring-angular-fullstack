package com.wisetechglobal.exercise.service;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.model.Employee;
import com.wisetechglobal.exercise.persistence.respository.EmployeeRepository;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Logic for crud operation on employee table.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService, ConstantsAware {
    private final DepartmentService departmentService;
    private final EmployeeRepository employeeRepository;

    /**
     * Created a new employee in the database
     *
     * @param employee employee details
     * @return base response
     */
    @Override
    public BaseResponse<Employee> createEmployee(Employee employee) {
        BaseResponse<Employee> response;
        if (CollectionUtils.isNotEmpty(employee.getDepartments()) && !departmentService.departmentsExist(employee.getDepartments().stream().map(Department::getId).collect(Collectors.toSet()))) {
            response = BaseResponse.<Employee>builder().errorStatus(HttpStatus.NOT_FOUND.value()).message(ERROR_DEPARTMENT_NOT_FOUND).build();
        } else {
            response = saveEmployee(employee);
        }
        return response;
    }

    /**
     * Read operation on employee table based on employee id
     *
     * @param id employee id
     * @return base response containing employee details
     */
    @Override
    public BaseResponse<Employee> getEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.map(employee -> BaseResponse.<Employee>builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).data(employee).build()).orElse(BaseResponse.<Employee>builder().message(ERROR_EMPLOYEE_NOT_FOUND).errorStatus(HttpStatus.NOT_FOUND.value()).build());
    }

    /**
     * update operation on employee table.
     *
     * @param employee updated employee details
     * @return base response
     */
    @Override
    public BaseResponse<Employee> updateEmployee(Employee employee) {
        BaseResponse<Employee> response;
        if(Objects.isNull(employee.getId())||employee.getId()<1)
            response = BaseResponse.<Employee>builder().errorStatus(HttpStatus.NOT_FOUND.value()).message(ERROR_DEPARTMENT_NOT_FOUND).build();
        else {
            if (!employeeRepository.existsById(employee.getId())) {
                response = BaseResponse.<Employee>builder().message(ERROR_EMPLOYEE_NOT_FOUND).errorStatus(HttpStatus.NOT_FOUND.value()).build();
            } else if (!departmentService.departmentsExist(employee.getDepartments().stream().map(Department::getId).collect(Collectors.toSet()))) {
                response = BaseResponse.<Employee>builder().errorStatus(HttpStatus.NOT_FOUND.value()).message(ERROR_DEPARTMENT_NOT_FOUND).build();
            } else {
                response = saveEmployee(employee);
            }
        }
        return response;
    }

    private BaseResponse<Employee> saveEmployee(Employee employee) {
        BaseResponse<Employee> response;
        Set<Department> requiredDepartments = departmentService.getRequiredDepartments();
        employee.getDepartments().addAll(requiredDepartments);
        Employee savedEmployee = employeeRepository.save(employee);
        response = BaseResponse.<Employee>builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).data(savedEmployee).build();
        return response;
    }

    /**
     * delete operation on employee table
     *
     * @param id employee id
     * @return base response containing appropriate status code
     */
    @Override
    public BaseResponse<Object> deleteEmployee(Long id) {
        BaseResponse<Object> response;
        if (!employeeRepository.existsById(id)) {
            response = BaseResponse.builder().message(ERROR_EMPLOYEE_NOT_FOUND).errorStatus(HttpStatus.NOT_FOUND.value()).build();
        } else {
            employeeRepository.deleteById(id);
            response = new BaseResponse<>();
        }
        return response;
    }

    @Override
    public BaseResponse<Set<Employee>> getAllEmployees(Integer pageNo, Integer fetchLimit) {
        Page<Employee> employees = employeeRepository.findAll(PageRequest.of(pageNo, fetchLimit));
        Set<Employee> employeeSet = new TreeSet<>(Comparator.comparingLong(Employee::getId));
        employeeSet.addAll(employees.toSet());
        BaseResponse<Set<Employee>> response = new BaseResponse<>();
        response.setData(employeeSet);
        return response;
    }

    @Override
    public BaseResponse<Long> getEmployeeCount() {
        BaseResponse<Long> response;
        long employeeCount = employeeRepository.count();
        response = BaseResponse.<Long>builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).data(employeeCount).build();
        return response;
    }
}
