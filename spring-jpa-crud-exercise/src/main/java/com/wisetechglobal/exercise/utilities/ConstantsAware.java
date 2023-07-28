package com.wisetechglobal.exercise.utilities;

public interface ConstantsAware {
    String PROCESSING_ERROR = "Processing Error: {0}";

    Integer SUCCESS_CODE = 0;

    String SUCCESS_MESSAGE = "Success";

    String ERROR_EMPLOYEE_NOT_FOUND = "Employee not found";
    String ERROR_DEPARTMENT_NOT_FOUND = "Department not found";
    String ERROR_INVALID_REQUEST = "Invalid Request";
    String ERROR_RECORD_NOT_FOUND = "Record not found";
    String ERROR_DEPARTMENT_UPDATE_IS_NOT_ALLOWED = "Update on this department is not allowed";
    String ERROR_DEPARTMENT_NAME_UNIQUE_CONSTRAINT_VIOLATION = "Department name already exist";
}
