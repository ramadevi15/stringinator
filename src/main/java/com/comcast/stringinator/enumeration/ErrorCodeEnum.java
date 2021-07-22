package com.comcast.stringinator.enumeration;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum ErrorCodeEnum {

    INVALID_DATA(BAD_REQUEST,1000, "Invalid String - No AlphaNumeric Char"),
    INVALID_INPUT(BAD_REQUEST, 1001, "Invalid input"),
    INVALID_QUERY_INPUT(BAD_REQUEST, 1002, "Invalid input"),
    INVALID_REQUEST(BAD_REQUEST,1003,"Invalid request"),
    NO_POPULAR_STRING(BAD_REQUEST,1004,"No popular string found");

    private final HttpStatus statusCode;
    private final int subErrorCode;
    private final String developerErrorMessage;

    ErrorCodeEnum(HttpStatus statusCode, int subErrorCode, String developerErrorMessage) {
        this.statusCode = statusCode;
        this.subErrorCode = subErrorCode;
        this.developerErrorMessage = developerErrorMessage;
    }

    public int subErrorCode() {
        return this.subErrorCode;
    }

    public HttpStatus statusCode() {
        return this.statusCode;
    }

    public String devMessage() {
        return this.developerErrorMessage;
    }

}
