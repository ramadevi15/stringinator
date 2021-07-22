package com.comcast.stringinator.exception.advice;

/**
 *Controller Advice to handle exception
 *
 * @author Rama Devi
 */

import com.comcast.stringinator.enumeration.ErrorCodeEnum;
import com.comcast.stringinator.exception.ApplicationException;
import com.comcast.stringinator.model.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static com.comcast.stringinator.enumeration.ErrorCodeEnum.INVALID_QUERY_INPUT;
import static com.comcast.stringinator.enumeration.ErrorCodeEnum.INVALID_REQUEST;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleAppException(ApplicationException exception, WebRequest request) {
        LOGGER.error("Application Exception occurred {}", exception::getErrorCodeEnum);
        return new ResponseEntity(buildErrorResponse(exception.getErrorCodeEnum()), HttpStatus.BAD_REQUEST);
    }

    /*Default Handler */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleException(ConstraintViolationException exception) {
        LOGGER.error("Constraint violations occurred {}", () -> exception);
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidFormatException.class, MismatchedInputException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handlerIllegalArgumentException(Exception exception,
                                                                  WebRequest webRequest) {
        LOGGER.error("Illegal argument exception occurred {}", () -> exception);
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        LOGGER.error("ConversionNotSupported exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGeneralException(HttpServletRequest req, Exception exception) {
        LOGGER.error("General exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        LOGGER.error("NoHandlerFound exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("MethodArgumentNotValid exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(ErrorCodeEnum.INVALID_INPUT, exception.getAllErrors().stream().findFirst().get().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException exception,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("ServletRequestBinding exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("HttpMessageNotReadable exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        LOGGER.error("Bind exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("HttpRequestMethodNotSupported exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("HttpMediaTypeNotSupported exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        LOGGER.error("Internal Exception occurred {}", () -> exception.getMessage());
        return new ResponseEntity(buildErrorResponse(INVALID_QUERY_INPUT), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse buildErrorResponse(ErrorCodeEnum errorCodeEnum, Object errorMessage) {
        LOGGER.info("Build error response with errorCodeEnum and error message");
        return ErrorResponse.builder()
                .errorCode(errorCodeEnum.subErrorCode())
                .errorMessage(errorMessage.toString()).build();
    }

    private ErrorResponse buildErrorResponse(ErrorCodeEnum errorCodeEnum) {
        LOGGER.info("Build error response with errorCodeEnum");
        return ErrorResponse.builder()
                .errorCode(errorCodeEnum.subErrorCode())
                .errorMessage(errorCodeEnum.devMessage()).build();
    }


}
