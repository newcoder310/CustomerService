package com.api.customers.exception;

import com.api.customers.model.CustomerServiceError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomerNotPresentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected @ResponseBody CustomerServiceError handleException(CustomerNotPresentException exception) {
        CustomerServiceError error = new CustomerServiceError();
        error.message(exception.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomerServiceError handleException(MethodArgumentTypeMismatchException exception) {

        String err = exception.getParameter().getExecutable().toString();

        CustomerServiceError error = new CustomerServiceError();
        error.message("Invalid Field Value: " + err);
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomerServiceError handleException(MethodArgumentNotValidException exception) {

        String err = exception.getParameter().getExecutable().toString();

        CustomerServiceError error = new CustomerServiceError();
        error.message("Request Validation Failed");
        return error;
    }

    @ExceptionHandler(ExternalCallFailedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomerServiceError handleException(ExternalCallFailedException exception) {

        CustomerServiceError error = new CustomerServiceError();
        error.message(exception.getMessage());
        return error;
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomerServiceError handleException(NumberFormatException exception) {

        CustomerServiceError error = new CustomerServiceError();
        error.message("Request Validation Failed");
        return error;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomerServiceError handleException(ConstraintViolationException exception) {
        CustomerServiceError error = new CustomerServiceError();
        error.message("Request Validation Failed");
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody CustomerServiceError handleException(Exception exception) {

        CustomerServiceError error = new CustomerServiceError();
        error.message("Internal Error");
        return error;
    }

}
