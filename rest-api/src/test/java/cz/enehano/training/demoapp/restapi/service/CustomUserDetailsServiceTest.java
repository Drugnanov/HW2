package cz.enehano.training.demoapp.restapi.service;

import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import cz.enehano.training.demoapp.restapi.security.CustomUserDetails;
import cz.enehano.training.demoapp.restapi.service.autentification.CustomUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {

    private User user = new User(1L, "MichalTest", "Sláma", "slama.michal@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), null);

    @Mock
    private UserRepository mockedUserRepository;

    private CustomUserDetailsService customUserDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customUserDetailsService = new CustomUserDetailsService(mockedUserRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        when(mockedUserRepository.findByEmail(user.getEmail())).thenAnswer(i -> Optional.of(user));
        UserDetails expectedDetails = new CustomUserDetails(user);

        UserDetails actualDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        assertEquals(actualDetails.getClass(), expectedDetails.getClass());
        assertEquals(actualDetails.getUsername(), expectedDetails.getUsername());
    }
}