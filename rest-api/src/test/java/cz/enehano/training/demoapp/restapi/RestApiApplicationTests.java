package cz.enehano.training.demoapp.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.enehano.training.demoapp.restapi.configuration.DatabaseInit;
import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import cz.enehano.training.demoapp.restapi.util.UserConvertor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {
        RestApiApplication.class,
})
public class RestApiApplicationTests {

    private static final String USER = "slama.michal84@gmail.com";
    private static final String USER_PASSWORD = "aaa";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserConvertor userConvertor;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user = new User("MichalTest", "Sláma", "test.user@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG");

    private List<User> initUsers = new ArrayList<>();

    @Test
    public void contextLoads() {
    }

    @Before
    public void setUp(){
        repository.deleteAllInBatch();
        User creator = null;
        for (User user : DatabaseInit.getInitUsers()) {
            user.setCreator(creator);
            User persistedUser = repository.save(user);
            if (creator == null) {
                creator = persistedUser;
            }
            initUsers.add(persistedUser);
        }
        user.setCreator(creator);
        initUsers.add(repository.save(user));
        initUsers.sort(Comparator.comparing(User::getSurname));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(userConvertor.convertToDto(initUsers));

        mvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void testGetUser() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(userConvertor.convertToDto(user));

        mvc.perform(get("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void testGetNonExistingUser() throws Exception {
        mvc.perform(get("/users/" + -1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUserUnauthorized() throws Exception {
        mvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateUser() throws Exception {
        mvc.perform(post("/users")
                .with(httpBasic(USER, USER_PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"firstName\": \"Created\",\n" +
                        "\"surname\": \"new\",\n" +
                        "\"email\": \"seznam@seznam.com\",\n" +
                        "\"phoneNumber\": \"+42052685265\",\n" +
                        "\"password\": \"aaa\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.firstName", is(equalTo("Created"))))
                .andExpect(jsonPath("$.surname", is(equalTo("new"))))
                .andExpect(jsonPath("$.email", is(equalTo("seznam@seznam.com"))))
                .andExpect(jsonPath("$.phoneNumber", is(equalTo("+42052685265"))))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.createdById", is(notNullValue())))
                .andExpect(jsonPath("$.created", is(notNullValue())));
    }

    @Test
    public void testUpdateUser() throws Exception {
        mvc.perform(put("/users/" + user.getId())
                .with(httpBasic(USER, USER_PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"id\": " + user.getId() + ",\n" +
                        "\"firstName\": \"updated\",\n" +
                        "\"surname\": \"changed\",\n" +
                        "\"email\": \"fake.faked@gmail.com\",\n" +
                        "\"phoneNumber\": \"+00000001\",\n" +
                        "\"password\": \"aaa\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(user.getId().intValue()))))
                .andExpect(jsonPath("$.firstName", is(equalTo("updated"))))
                .andExpect(jsonPath("$.surname", is(equalTo("changed"))))
                .andExpect(jsonPath("$.email", is(equalTo("fake.faked@gmail.com"))))
                .andExpect(jsonPath("$.phoneNumber", is(equalTo("+00000001"))))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.createdById", is(notNullValue())))
                .andExpect(jsonPath("$.created", is(notNullValue())));
    }

    @Test
    public void testUpdateUserUnauthorized() throws Exception {
        mvc.perform(put("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateNonExistingUser() throws Exception {
        mvc.perform(put("/users/-1")
                .with(httpBasic(USER, USER_PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"id\": -1,\n" +
                        "\"firstName\": \"updated\",\n" +
                        "\"surname\": \"changed\",\n" +
                        "\"email\": \"fake.faked@gmail.com\",\n" +
                        "\"phoneNumber\": \"+00000001\",\n" +
                        "\"password\": \"aaa\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mvc.perform(delete("/users/" + user.getId())
                .with(httpBasic(USER, USER_PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        mvc.perform(delete("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteNonExistingUser() throws Exception {
        mvc.perform(delete("/users/-1")
                .with(httpBasic(USER, USER_PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
