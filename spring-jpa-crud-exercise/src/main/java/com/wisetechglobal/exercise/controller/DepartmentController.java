package com.wisetechglobal.exercise.controller;


import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.service.DepartmentService;
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
 * CRUD operation on department entity
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    /**
     * Adding a new department to the system
     *
     * @param department request body
     * @return response entity containing base response
     */
    @PostMapping
    @ApiOperation(value = "Adds a new department")
    public ResponseEntity<BaseResponse<Department>> createDepartment(@RequestBody @Valid Department department) {
        return getResponseEntity(departmentService.createDepartment(department));
    }

    /**
     * Read operation on department table.
     *
     * @param id department id for fetching the department details
     * @return base response containing department details
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "get department details based on department id")
    public ResponseEntity<BaseResponse<Department>> getDepartment(@NotBlank @PathVariable("id") Long id) {
        return getResponseEntity(departmentService.getDepartment(id));
    }

    /**
     * To fetch the total count of departments in the system
     * @return count of department
     */
    @GetMapping("/count")
    @ApiOperation(value = "get total count of departments")
    public ResponseEntity<BaseResponse<Long>> getDepartmentCount(){
        return getResponseEntity(departmentService.getDepartmentCount());
    }
    /**
     * Get a list of all the department in the system
     * @param pageNo page no in the request param for pagination
     * @param fetchLimit size of each page
     * @return set of all the employee present in the system
     */
    @GetMapping
    public ResponseEntity<BaseResponse<Set<Department>>> getAllDepartments(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo, @RequestParam(value = "limit", required = false, defaultValue = "8") Integer fetchLimit) {
        return getResponseEntity(departmentService.getDepartments(pageNo,fetchLimit));
    }

    /**
     * Update operation on existing department
     *
     * @param department request body containing updated information
     * @param id         department id in department table.
     * @return base response containing updated department info
     */
    @PutMapping
    @ApiOperation(value = "updates an existing department")
    public ResponseEntity<BaseResponse<Department>> updateDepartment(@RequestBody @Valid Department department) {
        return getResponseEntity(departmentService.updateDepartment(department));
    }

    /**
     * Delete operation on department table.
     *
     * @param id department id of department which has to be deleted
     * @return Success base response in any situation exception if db in not available.
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "get department details based on department id")
    public ResponseEntity<BaseResponse<Object>> deleteDepartment(@NotBlank @PathVariable("id") Long id) {
        return getResponseEntity(departmentService.deleteDepartment(id));
    }

}
