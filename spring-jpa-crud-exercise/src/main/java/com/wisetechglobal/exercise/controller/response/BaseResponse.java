package com.wisetechglobal.exercise.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a generic response class to give a consistent structure to all the responses
 *
 * @param <T>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {
    private Integer errorStatus = ConstantsAware.SUCCESS_CODE;
    private String message = ConstantsAware.SUCCESS_MESSAGE;
    T data;

    public BaseResponse(int es, String msg) {
        errorStatus = es;
        message = msg;
    }
}
