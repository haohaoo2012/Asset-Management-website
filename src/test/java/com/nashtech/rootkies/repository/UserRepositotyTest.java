package com.nashtech.rootkies.repository;

import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.specs.SearchCriteria1;
import com.nashtech.rootkies.repository.specs.UserSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositotyTest {
    @Autowired
    UserRepository userRepository;

    /*@Test
    public void existsByUsernameTest() {
        Optional<User> user = userRepository.findByStaffCode("SD0001");
        assertNotNull(user);
        String username = user.get().getUsername();
        Boolean check = userRepository.existsByUsername(username);
        assertEquals(check, true);
    }

    @Test
    public void findByStaffCodeTest() {
        Optional<User> user = userRepository.findByStaffCode("SD0001");
        assertEquals("SD0001", user.get().getStaffCode());
    }

    @Test
    public void findAllUser() {
        Specification specification = new UserSpecification(
                new SearchCriteria1("staffCode", ":", "SD0001"));

        assertEquals(userRepository.findAll(specification, PageRequest.of(0, 1)).getTotalElements(), 1);
    }

    @Test
    public void findByUsernameTest() {
        Optional<User> option = userRepository.findByUsername("binhnv");
        assertEquals(true, option.isPresent());
    }*/
}
