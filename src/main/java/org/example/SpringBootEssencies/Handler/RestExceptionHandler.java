package org.example.SpringBootEssencies.Handler;

import lombok.extern.log4j.Log4j2;
import org.example.SpringBootEssencies.Exception.BadRequestException;
import org.example.SpringBootEssencies.Exception.ExceptionDetails;
import org.example.SpringBootEssencies.Exception.ExceptionTittle;
import org.example.SpringBootEssencies.Exception.ValidationExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionTittle> handleBadRequestException(BadRequestException badRequestException){
        return new ResponseEntity<>(
                ExceptionTittle.builder()
                        .localTime(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .tittle("Bad Request Exception")
                        .details(badRequestException.getMessage())
                        .developerMessage(badRequestException.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException exception,HttpHeaders headers,HttpStatus status,WebRequest request){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String field = fieldErrors.stream().map(FieldError::getField)
                .collect(Collectors.joining(", "));
        String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .localTime(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .tittle("Bad Request Exception")
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .fields(field)
                        .fieldsMessage(fieldMessage)
                        .build(), HttpStatus.BAD_REQUEST);
    }
    @Override
    protected  ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                              @Nullable Object body,
                                                              HttpHeaders headers,
                                                              HttpStatus status,
                                                              WebRequest request){
        ExceptionDetails build = ExceptionDetails.builder()
                .localTime(LocalDateTime.now())
                .status(status.value())
                .tittle(ex.getCause().getMessage())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();
        return new ResponseEntity<>(build,headers,status);
    }
}
