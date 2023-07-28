package com.wisetechglobal.exercise.automation;


import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.model.Employee;
import lombok.Data;
import org.springframework.stereotype.Component;
import io.cucumber.spring.ScenarioScope;

import java.util.Map;

@Data
@Component
@ScenarioScope
public class ScenarioContext {
    private BaseResponse<Employee> employeeBaseResponse;
    private BaseResponse<Department> departmentBaseResponse;
    private BaseResponse<Object> otherBaseResponse;
    private Long id;
    Department department;
    Employee employee;
    private Map<String,Object> genericMapObject;
}