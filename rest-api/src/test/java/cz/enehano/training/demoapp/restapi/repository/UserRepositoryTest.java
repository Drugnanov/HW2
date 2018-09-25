package cz.enehano.training.demoapp.restapi.repository;

import cz.enehano.training.demoapp.restapi.RestApiApplication;
import cz.enehano.training.demoapp.restapi.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        RestApiApplication.class,
})
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testOrderedStudentsBySurname() {
        List<User> orderedUsers = repository.findAllByOrderBySurnameAsc();
        Assert.assertTrue("Users collection is not sorted by surnames.", isSorted(orderedUsers));
    }

    private boolean isSorted(List<User> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).getSurname().compareTo(list.get(i).getSurname()) > 0)
                return false;
        }
        return true;
    }
}
