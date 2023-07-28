package com.wisetechglobal.exercise.service;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.respository.DepartmentRepository;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of curd logic on department table
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService, ConstantsAware {

    private final DepartmentRepository departmentRepository;

    /**
     * Creates a new department in the system
     *
     * @param department department information
     * @return Base response containing success or failure status code
     */
    @Override
    public BaseResponse<Department> createDepartment(Department department) {
        BaseResponse<Department> baseResponse;
        baseResponse = saveDepartment(department);
        return baseResponse;
    }

    private BaseResponse<Department> saveDepartment(Department department) {
        BaseResponse<Department> baseResponse;
        try {
            Department dbDepartment = departmentRepository.save(department);
            baseResponse = BaseResponse.<Department>builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).data(dbDepartment).build();
        } catch (DataIntegrityViolationException ex) {
            baseResponse = BaseResponse.<Department>builder().errorStatus(409).message(ERROR_DEPARTMENT_NAME_UNIQUE_CONSTRAINT_VIOLATION).build();
        }
        return baseResponse;
    }

    /**
     * updates existing department
     *
     * @param department update department details
     * @param id         department id
     * @return base response containing success or failure response
     */
    @Override
    public BaseResponse<Department> updateDepartment(Department department) {
        BaseResponse<Department> response;
        if(Objects.isNull(department.getId())||department.getId()<1)
            response = BaseResponse.<Department>builder().errorStatus(HttpStatus.NOT_FOUND.value()).message(ERROR_DEPARTMENT_NOT_FOUND).build();
        else {
            Optional<Department> optionalDepartment = departmentRepository.findById(department.getId());
            if (optionalDepartment.isEmpty())
                response = BaseResponse.<Department>builder().message(ERROR_DEPARTMENT_NOT_FOUND).errorStatus(HttpStatus.NOT_FOUND.value()).build();
            else {
                Department dbDepartment = optionalDepartment.get();
                if (dbDepartment.getReadOnly() && department.getReadOnly())
                    response = BaseResponse.<Department>builder().message(ERROR_DEPARTMENT_UPDATE_IS_NOT_ALLOWED).errorStatus(400).build();
                else {
                    response = saveDepartment(department);
                }
            }
        }
        return response;
    }

    /**
     * Get department details based in id
     *
     * @param id department id
     * @return base response containing department information
     */
    @Override
    public BaseResponse<Department> getDepartment(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        return optionalDepartment.map(department -> BaseResponse.<Department>builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).data(department).build()).orElse(BaseResponse.<Department>builder().message(ERROR_DEPARTMENT_NOT_FOUND).errorStatus(HttpStatus.NOT_FOUND.value()).build());
    }

    /**
     * Delete department from the system
     *
     * @param id department id which has to be deleted
     * @return success or failure base response
     */
    @Override
    public BaseResponse<Object> deleteDepartment(Long id) {
        BaseResponse<Object> response;
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isEmpty())
            response = BaseResponse.builder().message(ERROR_DEPARTMENT_NOT_FOUND).errorStatus(HttpStatus.NOT_FOUND.value()).build();
        else {
            Department department = optionalDepartment.get();
            if (Boolean.TRUE.equals(department.getReadOnly())) {
                response = BaseResponse.builder().message(ERROR_DEPARTMENT_UPDATE_IS_NOT_ALLOWED).errorStatus(400).build();
            } else {
                departmentRepository.delete(department);
                response = BaseResponse.builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).build();
            }
        }
        return response;
    }

    /**
     * Returns list of required departments, i.e. departments which has is_required set to true
     *
     * @return set of departments
     */
    @Override
    public Set<Department> getRequiredDepartments() {
        Set<Department> requiredDepartment = departmentRepository.findByRequired(true);
        if (CollectionUtils.isNotEmpty(requiredDepartment))
            return requiredDepartment;
        return new HashSet<>();
    }

    /**
     * Check if all the department ids exist in the database
     *
     * @param ids
     * @return
     */
    @Override
    public boolean departmentsExist(Set<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids))
            return departmentRepository.countByIdIn(ids) == ids.size();
        return true;
    }

    @Override
    public BaseResponse<Set<Department>> getDepartments(Integer pageNo, Integer fetchLimit) {
        Page<Department> departments = departmentRepository.findAll(PageRequest.of(pageNo, fetchLimit));
        Set<Department> departmentSet = new TreeSet<>(Comparator.comparingLong(Department::getId));
        departmentSet.addAll(departments.toSet());
        BaseResponse<Set<Department>> response = new BaseResponse<>();
        response.setData(departmentSet);
        return response;
    }

    @Override
    public BaseResponse<Long> getDepartmentCount() {
        BaseResponse<Long> response;
        long departmentCount = departmentRepository.count();
        response = BaseResponse.<Long>builder().errorStatus(SUCCESS_CODE).message(SUCCESS_MESSAGE).data(departmentCount).build();
        return response;
    }

}
