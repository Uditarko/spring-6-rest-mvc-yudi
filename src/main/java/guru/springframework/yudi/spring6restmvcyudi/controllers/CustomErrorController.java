package guru.springframework.yudi.spring6restmvcyudi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException methodArgumentNotValidException){
        return ResponseEntity.badRequest().body(methodArgumentNotValidException.getBindingResult().getFieldErrors());
    }
}
