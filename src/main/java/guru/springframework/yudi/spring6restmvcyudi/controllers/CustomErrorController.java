package guru.springframework.yudi.spring6restmvcyudi.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<Map<String, String>> errorList = methodArgumentNotValidException.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler
    ResponseEntity handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder resp = ResponseEntity.badRequest();
        /*pattern variable use*/
        if (exception.getCause().getCause() instanceof ConstraintViolationException violationException) {
            List<Map<String, String>> errorList = violationException.getConstraintViolations().stream()
                    .map((elem) -> {
                        Map<String, String> map = new HashMap<>();
                        map.put(elem.getPropertyPath().toString(), elem.getMessage());
                        return map;
                    })
                    .toList();
            return resp.body(errorList);
        }
        return resp.build();
    }
}
