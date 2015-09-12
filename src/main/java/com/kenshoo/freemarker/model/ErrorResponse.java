package com.kenshoo.freemarker.model;

/**
 * Created by Pmuruge on 8/30/2015.
 */
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorDescription;

    public ErrorResponse(ErrorCode errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
