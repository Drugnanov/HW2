package cz.enehano.training.demoapp.restapi.service.autentification;

import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import cz.enehano.training.demoapp.restapi.security.CustomUserDetails;
import cz.enehano.training.demoapp.restapi.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UserNotFoundException(email);
        }
        return new CustomUserDetails(user.get());
    }
}

