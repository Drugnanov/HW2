package cz.enehano.training.demoapp.restapi.controller;

import cz.enehano.training.demoapp.restapi.service.exception.UserWithEmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class UserExistsAdvice {

    @ResponseBody
    @ExceptionHandler(UserWithEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String userExistsAdvice(UserWithEmailAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
