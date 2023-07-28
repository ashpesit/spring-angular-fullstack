package com.wisetechglobal.exercise.utilities;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtils {
    private ControllerUtils(){
        throw new IllegalStateException("Controller Utility class");
    }
    public static  <T> ResponseEntity<BaseResponse<T>> getResponseEntity(BaseResponse<T> response) {
        if (response.getErrorStatus() != 0) {
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getErrorStatus()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
