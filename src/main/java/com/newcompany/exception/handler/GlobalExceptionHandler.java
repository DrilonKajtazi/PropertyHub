package com.newcompany.exception.handler;

import com.newcompany.exception.model.ErrorDTO;
import com.newcompany.exception.model.UserNotLoggedInException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ErrorDTO> generateNotLoggedInException(UserNotLoggedInException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }
}
