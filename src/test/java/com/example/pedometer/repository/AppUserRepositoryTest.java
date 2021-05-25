/*package com.example.pedometer.repository;

import com.example.pedometer.model.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class AppUserRepositoryTest {

    @Autowired
    AppUserRepository appUserRepository;

    AppUser mockUser1;

    @BeforeEach
    void init(){
        mockUser1 = new AppUser()
                .setFirstName("Mickey")
                .setLastName("Mouse")
                .setPassword("mickey123")
                .setEmail("mickey@email.com");

        appUserRepository.save(mockUser1);
    }

    @AfterEach
    void cleanUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void findByEmailTest() {
        AppUser expected = mockUser1;

        String testEmail = expected.getEmail();

        AppUser actual = appUserRepository.findByEmail(testEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        assertEquals(expected.getFirstName(), actual.getFirstName());
    }

}*/
