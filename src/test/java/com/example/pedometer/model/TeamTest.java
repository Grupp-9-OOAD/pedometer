package com.example.pedometer.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created by Julia Wigenstedt
 * Date: 2021-05-24
 * Time: 15:18
 * Project: pedometer
 * Copyright: MIT
 */
@RequiredArgsConstructor
public class TeamTest {

    private final Team testTeam = new Team()
            .setTeamName("Test team");


    @Test
    void addSameMemberTwiceThrowsException() {
        AppUser testUser = new AppUser()
                .setEmail("test@test.com")
                .setFirstName("Test")
                .setLastName("Testsson")
                .setPassword("test");

        testTeam.addTeamMember(testUser);

        assertThrows(IllegalArgumentException.class, () -> testTeam.addTeamMember(testUser));
    }


}
