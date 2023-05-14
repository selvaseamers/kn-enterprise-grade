package kn.service.citylist.be.exception.advice;

import kn.service.citylist.be.exception.ApplicationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(1)
public class ApplicationExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationExceptionAdvice.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleAppException(ApplicationException exception, WebRequest request) {
        LOGGER.error("Exception handled sending message {}", exception.getMessage());
        exception.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAppException(Exception exception, WebRequest request) {
        LOGGER.error("Exception handled sending message {}", exception.getMessage());
        exception.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus statusCode, String message) {
        return ResponseEntity.status(statusCode)
                .body(message);
    }
}
