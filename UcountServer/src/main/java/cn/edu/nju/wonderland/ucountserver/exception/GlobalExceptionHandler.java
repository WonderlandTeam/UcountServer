package cn.edu.nju.wonderland.ucountserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Map<String, String> getResponseMap(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("error", message);
        return result;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> resourceNotFoundHandler(ResourceNotFoundException e) {
        return getResponseMap(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceConflictException.class)
    public Map<String, String> resourceConflictHandler(ResourceConflictException e) {
        return getResponseMap(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    public Map<String, String> invalidRequestHandler(InvalidRequestException e) {
        return getResponseMap(e.getMessage());
    }

}
