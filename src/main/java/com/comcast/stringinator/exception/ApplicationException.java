package com.comcast.stringinator.exception;

import com.comcast.stringinator.enumeration.ErrorCodeEnum;

public class ApplicationException extends RuntimeException{

    private final transient ErrorCodeEnum errorCodeEnum;

    public ApplicationException(ErrorCodeEnum errorCode) {
        super(errorCode.statusCode().getReasonPhrase());
        this.errorCodeEnum = errorCode;
    }
    public ApplicationException(ErrorCodeEnum errorCode, Exception exception) {
        super(errorCode.statusCode().getReasonPhrase());
        this.errorCodeEnum = errorCode;
        super.initCause(exception);
    }

    public ErrorCodeEnum getErrorCodeEnum() {
        return errorCodeEnum;
    }
}
