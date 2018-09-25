package cz.enehano.training.demoapp.restapi.service;

import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceDefaultTest {

    private User user = new User(1L, "MichalTest", "Sláma", "slama.michal@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), null);
    private User userEdited = new User(1L, "MichalUpdated", "Sláma", "slama.michal@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), null);
    private User userSecond = new User(2L, "Michal", "Sláma", "atest.user@gmail.com", "+42055555555", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), user);

    private List<User> initUsers = new ArrayList<User>() {{
        add(user);
        add(userSecond);
    }};

    @Mock
    private UserRepository mockedUserRepository;
    @Mock
    private PasswordEncoder mockedPasswordEncoder;

    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedPasswordEncoder.encode(any(String.class))).thenReturn("$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG");
        userService = new UserServiceDefault(mockedUserRepository, mockedPasswordEncoder);
    }

    @Test
    public void testGetAllUsers() {
        when(mockedUserRepository.findAllByOrderBySurnameAsc()).thenReturn(initUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertThat(actualUsers, containsInAnyOrder(initUsers.toArray()));
    }

    @Test
    public void testGetUser() {
        when(mockedUserRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User actualUser = userService.getUserById(user.getId());

        assertEquals(actualUser, user);
    }

    @Test
    public void testCreateUser() {
        when(mockedUserRepository.save(any(User.class))).thenAnswer(i -> userSecond);
        when(mockedUserRepository.findByEmail(user.getEmail())).thenAnswer(i -> Optional.of(user));

        User actualUser = userService.createUser(userSecond, user.getEmail());

        assertEquals(actualUser, userSecond);
    }

    @Test
    public void testUpdateUser() {
        when(mockedUserRepository.save(any(User.class))).thenAnswer(i -> userEdited);
        when(mockedUserRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User updatedUser = userService.updateUser(user.getId(), user);

        assertEquals(updatedUser, userEdited);
    }

    @Test
    public void testDeleteUser() {
        when(mockedUserRepository.findById(user.getId())).thenAnswer(i -> Optional.of(user));

        userService.deleteUser(user.getId());

        verify(mockedUserRepository, times(1)).deleteById(user.getId());
    }
}