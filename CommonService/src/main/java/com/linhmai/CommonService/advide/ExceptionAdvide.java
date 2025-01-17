package com.linhmai.CommonService.advide;

import com.linhmai.CommonService.common.CommonException;
import com.linhmai.CommonService.common.ErrorMessage;
import com.linhmai.CommonService.common.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionAdvide {
    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleCommonException(CommonException ex) {
        log.error(String.format("Common error: %s %s %s", ex.getCode(), ex.getStatus(), ex.getMessage()));
        ErrorMessage errorMessage = new ErrorMessage(ex.getCode(), ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(errorMessage, ex.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        log.error("Unknown internal server error: " + ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage("999", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidateException(ValidateException ex) {
        return new ResponseEntity<>(ex.getMessageMap(), ex.getStatus());
    }
}
