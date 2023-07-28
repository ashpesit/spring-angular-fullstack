package com.wisetechglobal.exercise.controller;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Employee;
import com.wisetechglobal.exercise.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

import static com.wisetechglobal.exercise.utilities.ControllerUtils.getResponseEntity;

/**
 * Crud operation on employee entity
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/employee")
public class EmployeeController {
    public final EmployeeService employeeService;

    /**
     * Create a new employee entry in employee table.
     *
     * @param employee request body containing employee detains
     * @return Base response containing details of newly created employee entry
     */
    @PostMapping
    @ApiOperation(value = "Adds a new employee")
    public ResponseEntity<BaseResponse<Employee>> createEmployee(@RequestBody @Valid Employee employee) {
        return getResponseEntity(employeeService.createEmployee(employee));
    }

    /**
     * Read operation on employee table
     *
     * @param id employee id
     * @return Base response containing details of employee
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "get employee details based on employee id")
    public ResponseEntity<BaseResponse<Employee>> getEmployee(@NotBlank @PathVariable("id") Long id) {
        return getResponseEntity(employeeService.getEmployee(id));
    }

    /**
     * To fetch the total count of departments in the system
     * @return count of department
     */
    @GetMapping("/count")
    @ApiOperation(value = "get total count of departments")
    public ResponseEntity<BaseResponse<Long>> getDepartmentCount(){
        return getResponseEntity(employeeService.getEmployeeCount());
    }

    /**
     * Get a list of all the employee in the system
     * @param pageNo page no in the request param for pagination
     * @param fetchLimit size of each page
     * @return set of all the employee present in the system
     */
    @GetMapping
    public ResponseEntity<BaseResponse<Set<Employee>>> getAllEmployee(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo, @RequestParam(value = "limit", required = false, defaultValue = "8") Integer fetchLimit){
        return getResponseEntity(employeeService.getAllEmployees(pageNo, fetchLimit));
    }


    /**
     * Update operation on employee table
     *
     * @param employee updated employee information
     * @return base response containing updated employee details
     */
    @PutMapping
    @ApiOperation(value = "updates an existing employee")
    public ResponseEntity<BaseResponse<Employee>> updateEmployee(@RequestBody @Valid Employee employee) {
        return getResponseEntity(employeeService.updateEmployee(employee));
    }

    /**
     * Read operation on employee table
     *
     * @param id employee id from employee table
     * @return base response with employee details
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "get employee details based on employee id")
    public ResponseEntity<BaseResponse<Object>> deleteEmployee(@NotBlank @PathVariable("id") Long id) {
        return getResponseEntity(employeeService.deleteEmployee(id));
    }
}
