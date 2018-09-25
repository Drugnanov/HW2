package cz.enehano.training.demoapp.restapi.configuration;

import cz.enehano.training.demoapp.restapi.model.User;
import cz.enehano.training.demoapp.restapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class DatabaseInit {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            User creator = null;
            for (User user : getInitUsers()) {
                user.setCreator(creator);
                User userPersisted = repository.save(user);
                if (creator == null){
                    creator = userPersisted;
                }
                log.info("Preloading " + userPersisted);
            }
        };
    }

    public static List<User> getInitUsers() {
        return INIT_USERS;
    }

    private static final List<User> INIT_USERS = new ArrayList<User>() {{
        //password aaa
        add(new User("Michal", "Sláma", "slama.michal84@gmail.com", "+42052685263", "$2a$10$SSkOQSL0Twz/LL4Oz7XOb.6HU5bol4kPSzdCR52yMskIEZxtLgCwG"));
        add(new User("Jan", "Pokorný", "fake.fake@gmail.com", "+42052685264", "$2a$10$hRX3sTHRu9ugD.Xk9W7mH..8uLlCP2ttZqwLJZ1s9KqTHImUpq.YC"));
    }};
}
