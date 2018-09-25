package cz.enehano.training.demoapp.restapi.controller;

import cz.enehano.training.demoapp.restapi.dto.UserDto;
import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import cz.enehano.training.demoapp.restapi.service.UserService;
import cz.enehano.training.demoapp.restapi.service.UserServiceDefault;
import cz.enehano.training.demoapp.restapi.util.UserConvertor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private User user = new User(1L, "MichalTest", "Sláma", "slama.michal@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), null);
    private User userNew = new User(1L, "MichalUpdated", "Sláma", "slama.michal@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), null);
    private User userSecond = new User(2L, "Michal", "Sláma", "atest.user@gmail.com", "+42055555555", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG", LocalDateTime.now(), user);

    private List<User> initUsers = new ArrayList<User>() {{
        add(user);
        add(userSecond);
    }};

    @Mock
    private UserRepository mockedUserRepository;
    @Mock
    private PasswordEncoder mockedPasswordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;

    private UserController userController;
    private UserConvertor userConvertor = new UserConvertor(new ModelMapper());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedPasswordEncoder.encode(any(String.class))).thenReturn("$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG");
        UserService userService = new UserServiceDefault(mockedUserRepository, mockedPasswordEncoder);
        userController = new UserController(userService, userConvertor);

        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    public void testGetAllUsers() {
        when(mockedUserRepository.findAllByOrderBySurnameAsc()).thenReturn(initUsers);

        List<UserDto> actualUsers = userController.getAllUsers();

        List<UserDto> expectedUsers = userConvertor.convertToDto(initUsers);
        assertThat(actualUsers, containsInAnyOrder(expectedUsers.toArray()));
    }

    @Test
    public void testGetUser() {
        when(mockedUserRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto actualUser = userController.getUser(user.getId());

        UserDto expectedUser = userConvertor.convertToDto(user);
        assertThat(actualUser, is(equalTo(expectedUser)));
    }

    @Test
    public void testCreateUser() {
        when(mockedUserRepository.save(any(User.class))).thenAnswer(i -> userSecond);
        when(mockedUserRepository.findByEmail(user.getEmail())).thenAnswer(i -> Optional.of(user));

        UserDto userToCreate = userConvertor.convertToDto(userSecond);
        UserDto actualUser = userController.createUser(userToCreate, authentication);

        assertEquals(actualUser, userToCreate);
    }

    @Test
    public void testUpdateUser() {
        when(mockedUserRepository.save(any(User.class))).thenAnswer(i -> userNew);
        when(mockedUserRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto updatedUser = userController.updateUser(userConvertor.convertToDto(user), user.getId());

        assertEquals(updatedUser, userConvertor.convertToDto(userNew));
    }

    @Test
    public void testDeleteUser() {
        when(mockedUserRepository.findById(user.getId())).thenAnswer(i -> Optional.of(user));

        userController.deleteUser(user.getId());

        verify(mockedUserRepository, times(1)).deleteById(user.getId());
    }
}