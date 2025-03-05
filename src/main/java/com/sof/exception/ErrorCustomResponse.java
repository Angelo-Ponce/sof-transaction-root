package com.sof.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCustomResponse<T> {
    private Integer code;
    private String message;
    private List<String> errors;
    private T data;
}
