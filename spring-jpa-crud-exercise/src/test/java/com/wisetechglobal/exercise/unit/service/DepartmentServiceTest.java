package com.wisetechglobal.exercise.unit.service;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;
import com.wisetechglobal.exercise.persistence.respository.DepartmentRepository;
import com.wisetechglobal.exercise.service.DepartmentService;
import com.wisetechglobal.exercise.service.DepartmentServiceImpl;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.wisetechglobal.exercise.Utilities.assertDepartmentMatches;
import static com.wisetechglobal.exercise.Utilities.assertResponseStatus;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest implements ConstantsAware {
    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentService ref;
    private DepartmentServiceImpl concreteRef;

    private Department getDepartment() {
        return Department.builder().id(1L).name("defaultDepartment").required(false).readOnly(false).build();
    }

    private Department getReadOnlyDepartment() {
        return Department.builder().id(1L).name("defaultDepartment").required(true).readOnly(true).build();
    }

    @BeforeEach
    void init() {
        concreteRef = new DepartmentServiceImpl(departmentRepository);
        ref = concreteRef;
    }

    @Test

    void createDepartment_DatabaseFailure_ThrowsDataAccessException() {
        Department department = getDepartment();
        doThrow(DataRetrievalFailureException.class).when(departmentRepository).save(department);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.createDepartment(department));
    }

    @Test
    void createDepartment_DuplicateDepartmentName_ReturnsErrorBaseResponseWith409Status() {
        Department department = getDepartment();
        doThrow(DataIntegrityViolationException.class).when(departmentRepository).save(department);
        BaseResponse<Department> response = ref.createDepartment(department);
        assertResponseStatus(response, HttpStatus.CONFLICT.value(), ERROR_DEPARTMENT_NAME_UNIQUE_CONSTRAINT_VIOLATION);
    }

    @Test
    void createDepartment_SaveSuccessful_ReturnsSuccessBaseResponseWith0Status() {
        Department department = getDepartment();
        doReturn(department).when(departmentRepository).save(department);
        BaseResponse<Department> response = ref.createDepartment(department);
        assertResponseStatus(response, 0, SUCCESS_MESSAGE);
        assertNotNull(response.getData());
        assertDepartmentMatches(department, response.getData());
    }

    @Test
    void updateDepartment_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(departmentRepository).findById(1L);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.updateDepartment(getDepartment()));
    }

    @Test
    void updateDepartment_DepartmentNotFound_ReturnsErrorBaseResponseWith404Status() {
        doReturn(Optional.empty()).when(departmentRepository).findById(1L);
        BaseResponse<Department> response = ref.updateDepartment(getDepartment());
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_DEPARTMENT_NOT_FOUND);
    }

    @Test
    void updateDepartment_ReadOnlyDepartment_ReturnsErrorBaseResponseWith400Status() {
        Department department = getReadOnlyDepartment();
        doReturn(Optional.of(department)).when(departmentRepository).findById(1L);
        BaseResponse<Department> response = ref.updateDepartment(department);
        assertResponseStatus(response, HttpStatus.BAD_REQUEST.value(), ERROR_DEPARTMENT_UPDATE_IS_NOT_ALLOWED);
    }

    @Test
    void updateDepartment_DuplicateDepartmentName_ReturnsErrorBaseResponseWith409Status() {
        Department department = getDepartment();
        doReturn(Optional.of(department)).when(departmentRepository).findById(1L);
        doThrow(DataIntegrityViolationException.class).when(departmentRepository).save(department);
        BaseResponse<Department> response = ref.updateDepartment(department);
        assertResponseStatus(response, HttpStatus.CONFLICT.value(), ERROR_DEPARTMENT_NAME_UNIQUE_CONSTRAINT_VIOLATION);
    }

    @Test
    void updateDepartment_SaveSuccessful_ReturnsBaseResponseWith0Status() {
        Department department = getDepartment();
        doReturn(department).when(departmentRepository).save(department);
        doReturn(Optional.of(department)).when(departmentRepository).findById(1L);
        BaseResponse<Department> response = ref.updateDepartment(department);
        assertResponseStatus(response, 0, SUCCESS_MESSAGE);
    }

    @Test
    void getDepartment_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(departmentRepository).findById(1L);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.getDepartment(1L));
    }

    @Test
    void getDepartment_DepartmentNotFound_ReturnsErrorBaseResponseWith404Status() {
        doReturn(Optional.empty()).when(departmentRepository).findById(1L);
        BaseResponse<Department> response = ref.getDepartment(1L);
        assertResponseStatus(response, HttpStatus.NOT_FOUND.value(), ERROR_DEPARTMENT_NOT_FOUND);
    }

    @Test
    void getDepartment_DepartmentFound_ReturnsSuccessBaseResponseWith0Status() {
        doReturn(Optional.of(getDepartment())).when(departmentRepository).findById(1L);
        BaseResponse<Department> response = ref.getDepartment(1L);
        assertResponseStatus(response, 0, SUCCESS_MESSAGE);
    }

    @Test
    void deleteDepartment_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(departmentRepository).findById(1L);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.deleteDepartment(1L));
    }

    @Test
    void deleteDepartment_DeleteReadOnlyDepartment_ReturnsErrorBaseResponseWith400Status() {
        doReturn(Optional.of(getReadOnlyDepartment())).when(departmentRepository).findById(1L);
        BaseResponse<Object> response = ref.deleteDepartment(1L);
        assertResponseStatus(response, HttpStatus.BAD_REQUEST.value(), ERROR_DEPARTMENT_UPDATE_IS_NOT_ALLOWED);
    }

    @Test
    void deleteDepartment_DeleteSuccessful_ReturnsBaseResponseWith0Status() {
        doReturn(Optional.of(getDepartment())).when(departmentRepository).findById(1L);
        BaseResponse<Object> response = ref.deleteDepartment(1L);
        assertResponseStatus(response, 0, SUCCESS_MESSAGE);
    }

    @Test
    void getRequiredDepartment_DatabaseFailure_ThrowsDataAccessException() {
        doThrow(DataRetrievalFailureException.class).when(departmentRepository).findByRequired(Boolean.TRUE);
        assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> ref.getRequiredDepartments());
    }

    @Test
    void getRequiredDepartment_DataNotFound_ReturnEmptySet() {
        doReturn(Collections.emptySet()).when(departmentRepository).findByRequired(Boolean.TRUE);
        Set<Department> response = ref.getRequiredDepartments();
        assertTrue(CollectionUtils.isEmpty(response));
    }

    @Test
    void getRequiredDepartment_DataFound_ReturnDepartmentSetWhereRequiredIsTrue() {
        Set<Department> departments = new HashSet<>();
        departments.add(Department.builder().id(3L).name("dep3").required(true).build());
        doReturn(departments).when(departmentRepository).findByRequired(Boolean.TRUE);
        Set<Department> departments1 = ref.getRequiredDepartments();
        assertTrue(CollectionUtils.isNotEmpty(departments1));
    }

    @Test
    void departmentsExist_EmptyDepartmentIdSet_ReturnTrue() {
        Boolean departmentsExist = ref.departmentsExist(Collections.emptySet());
        assertTrue(departmentsExist);
    }

    @Test
    void departmentsExist_AllDepartmentExist_ReturnTrue() {
        doReturn(1).when(departmentRepository).countByIdIn(Collections.singleton(1L));
        Boolean departmentsExist = ref.departmentsExist(Collections.singleton(1L));
        assertTrue(departmentsExist);
    }

    @Test
    void departmentsExist_AllDepartmentExist_ReturnFalse() {
        doReturn(0).when(departmentRepository).countByIdIn(Collections.singleton(1L));
        Boolean departmentsExist = ref.departmentsExist(Collections.singleton(1L));
        assertFalse(departmentsExist);
    }
}
