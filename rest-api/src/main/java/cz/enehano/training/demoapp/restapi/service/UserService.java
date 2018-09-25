package cz.enehano.training.demoapp.restapi.service;

import cz.enehano.training.demoapp.restapi.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(User user, String email);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
