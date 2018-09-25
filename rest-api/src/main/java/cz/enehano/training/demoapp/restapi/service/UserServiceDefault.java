package cz.enehano.training.demoapp.restapi.service;

import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import cz.enehano.training.demoapp.restapi.service.exception.UserNotFoundException;
import cz.enehano.training.demoapp.restapi.service.exception.UserWithEmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceDefault implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceDefault(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAllByOrderBySurnameAsc();
    }

    @Override
    public User createUser(User user, String creatorEmail) {
        Optional<User> userPersisted = this.userRepository.findByEmail(user.getEmail());
        if (userPersisted.isPresent()) {
            throw new UserWithEmailAlreadyExistsException(user.getEmail());
        }
        Optional<User> creator = this.userRepository.findByEmail(creatorEmail);
        if (!creator.isPresent()) {
            throw new UserNotFoundException(creatorEmail);
        }
        user.setCreator(creator.get());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> userOpt = this.userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(id);
        }
        user.setId(id);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setCreated(userOpt.get().getCreated());
        user.setCreator(userOpt.get().getCreator());
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> userOpt = this.userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(id);
        }
        this.userRepository.deleteById(id);
    }
}
