package cz.enehano.training.demoapp.restapi.service.exception;

public class UserWithEmailAlreadyExistsException extends RuntimeException {

    public UserWithEmailAlreadyExistsException(String email) {
        super("User with email " + email + " already exists.");
    }
}
