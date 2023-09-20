package com.api.customers.exception;

import com.api.customers.model.CustomerServiceError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleException_CustomerNotPresentException() {
        CustomerNotPresentException exception = new CustomerNotPresentException("test message");
        CustomerServiceError error = handler.handleException(exception);

        assertNotNull(error);
        assertEquals("test message", error.getMessage());
    }

    @Test
    public void testHandleException_ExternalCallFailedException() {
        ExternalCallFailedException exception = new ExternalCallFailedException("test message");
        CustomerServiceError error = handler.handleException(exception);

        assertNotNull(error);
        assertEquals("test message", error.getMessage());
    }

    @Test
    public void testHandleException_NumberFormatException() {
        NumberFormatException exception = new NumberFormatException();
        CustomerServiceError error = handler.handleException(exception);

        assertNotNull(error);
        assertEquals("Request Validation Failed", error.getMessage());
    }

}
