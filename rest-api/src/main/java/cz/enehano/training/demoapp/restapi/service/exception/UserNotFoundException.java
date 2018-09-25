package cz.enehano.training.demoapp.restapi.service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User with id " + id + " does not exist.");
    }

    public UserNotFoundException(String email) {
        super("User with email " + email + " does not exist.");
    }
}
