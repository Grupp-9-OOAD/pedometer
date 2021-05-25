package com.example.pedometer.service;


import com.example.pedometer.DTO.AppUserResponse;
import com.example.pedometer.model.AppUser;
import com.example.pedometer.model.Steps;
import com.example.pedometer.model.Team;
import com.example.pedometer.model.Steps;
import com.example.pedometer.repository.AppUserRepository;
import com.example.pedometer.repository.StepsRepository;
import com.example.pedometer.repository.TeamRepository;
import org.hamcrest.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    AppUserRepository mockAppUserRepository;

    @Mock
    StepsRepository mockStepsRepository;

    @Mock
    TeamRepository mockTeamRepository;

    AppUserService appUserService;

    List<AppUser> mockList;


    AppUser mockUser1;
    AppUser mockUser2;
    AppUser mockUser3;

    Team mockTeam;

    Steps mockStep1;

    @BeforeEach
    void init() {


        mockUser1 = new AppUser()
                .setFirstName("Mickey")
                .setLastName("Mouse")
                .setPassword("mickey123")
                .setEmail("mickey@email.com");
        mockUser2 = new AppUser()
                .setFirstName("Donald")
                .setLastName("Duck")
                .setPassword("donald123")
                .setEmail("donald@email.com");
        mockUser3 = new AppUser()
                .setFirstName("Goofy")
                .setLastName("Goof")
                .setPassword("goofy123")
                .setEmail("goofy@email.com");

        List<AppUser> mockedMembers = new ArrayList<>();
        mockedMembers.add(mockUser1);
        mockedMembers.add(mockUser2);
        mockedMembers.add(mockUser3);

        mockTeam = new Team()
                .setTeamName("Mocked")
                .setTeamMembers(mockedMembers);

        mockList = Arrays.asList(mockUser1,mockUser2,mockUser3);


        appUserService = new AppUserService(mockAppUserRepository, mockStepsRepository, mockTeamRepository);
    }

    @Test
    void removeUserFromTeamTest() {
        assertEquals(3, mockTeam.getTeamMembers().size());

        when(mockTeamRepository.findByTeamName(mockTeam.getTeamName()))
                .thenReturn(java.util.Optional.ofNullable(mockTeam));
        assertEquals(mockTeamRepository.findByTeamName(mockTeam.getTeamName()).get().getTeamName(), "Mocked");
        assertEquals(3, mockTeamRepository.findByTeamName(mockTeam.getTeamName()).get().getTeamMembers().size());
        appUserService.removeFromTeam(mockUser1.getEmail(), mockTeam.getTeamName());
        assertEquals(2, mockTeamRepository.findByTeamName(mockTeam.getTeamName()).get().getTeamMembers().size());

        verify(mockTeamRepository, times(4)).findByTeamName(anyString());
        verify(mockTeamRepository, times(1)).save(any());


    }

    @Test
    void getAllUsersTest() {

        int notCorrectListSize = 1000;

        when(mockAppUserRepository.findAll())
                .thenReturn(mockList);


        List<AppUserResponse> expected = mockList.stream()
                .map(AppUser::toResponse)
                .collect(Collectors.toList());

        List<AppUserResponse> actual = appUserService.getAll();

        assertEquals(actual.size(),expected.size());

        assertNotEquals(actual.size(), notCorrectListSize);

        assertEquals(actual.get(0).getEmail(),expected.get(0).getEmail());

        assertEquals(actual.get(1).getEmail(),expected.get(1).getEmail());

        assertEquals(actual.get(2).getEmail(),expected.get(2).getEmail());

        assertNotEquals(actual.get(0).getEmail(), expected.get(1).getEmail());

        assertThrows(IndexOutOfBoundsException.class, () -> actual.get(10));

        verify(mockAppUserRepository, times(1))
                .findAll();
    }

    @Test
    void updatePasswordTest() {

        String newPassword = "newPassword";
        String wrongPassword = "thisPasswordIsForTestingWrongPassword";

        when(mockAppUserRepository.findByEmail(mockUser1.getEmail()))
                .thenReturn(java.util.Optional.ofNullable(mockUser1));

        when(mockAppUserRepository.save(mockUser1))
                .thenReturn(mockUser1.setPassword(newPassword));

        AppUserResponse expected = mockUser1.setPassword(newPassword)
                                                .toResponse();

        AppUserResponse actual = appUserService.updatePassword(newPassword,mockUser1.getEmail());

        assertEquals(expected.getPassword(), actual.getPassword());

        assertNotEquals(wrongPassword, actual.getPassword());

        verify(mockAppUserRepository, times(1))
                .save(any());

        verify(mockAppUserRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void getStepsOfIfAppUserFound() {

        String userEmail = mockUser1.getEmail();
        int expected = 100;
        int notExpected = 100000;

        mockStep1 = new Steps()
                .setSteps(expected)
                .setDate(LocalDate.of(2020, 1, 5));

        mockUser1.getSteps().add(mockStep1);

        when(mockAppUserRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(mockUser1));

        int actual = appUserService.getStepsOfAppUser(userEmail);

        assertEquals(expected,actual);

        assertNotEquals(notExpected,actual);

        verify(mockAppUserRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void getStepsOfIfNotAppUserFound() {

        String userEmail = mockUser1.getEmail();

        when(mockAppUserRepository.findByEmail(userEmail))
                .thenReturn(null);

        assertThrows(NullPointerException.class, () -> appUserService.getStepsOfAppUser(mockUser1.getEmail()));

        verify(mockAppUserRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void deleteUserTest() {

        String email = "mickey@email.com";
        String wrongmail = "wrong@email.com";
        String password = "mickey123";
        String wrongPassword = "wrong123";

        when(mockAppUserRepository.findByEmail(mockUser1.getEmail()))
                .thenReturn(java.util.Optional.ofNullable(mockUser1));

        assertEquals("User with email: " + email + " has been deleted", appUserService.deleteAppUser(email, password));

        assertThrows(ResponseStatusException.class, () -> appUserService.deleteAppUser(email, wrongPassword));

        assertThrows(ResponseStatusException.class, () -> appUserService.deleteAppUser(wrongmail, password));

        verify(mockAppUserRepository, times(1))
                .deleteById(any());
    }



    @Test
    void addStepsToUserTest() {
        List<Steps> mockStepsList= new ArrayList<>();
        mockUser1.setSteps(mockStepsList);

        Steps mockSteps = new Steps();
        mockSteps.setDate(LocalDate.now());
        mockSteps.setSteps(500);



        when(mockAppUserRepository.findByEmail(mockUser1.getEmail()))
                .thenReturn(java.util.Optional.ofNullable(mockUser1));


        when(mockAppUserRepository.save(mockUser1))
                .thenReturn(mockUser1);


        mockStepsList.add(mockSteps);
        LocalDate actual = appUserService.addStepsToUser(mockUser1, 500, null).getSteps().get(0).getDate();
        LocalDate expected = LocalDate.now();

        assertThrows(ResponseStatusException.class, () -> appUserService.addStepsToUser(mockUser2, 300, LocalDate.now()));

        assertEquals(expected,actual);

        int expectedListSize = mockUser1.getSteps().size() + 1;
        int expectedListSize2 = mockUser1.getSteps().size();

        int actualListSize2 = appUserService.addStepsToUser(mockUser1, 500, LocalDate.now()).getSteps().size();

        int actualListSize = appUserService.addStepsToUser(mockUser1, 500, LocalDate.of(2021,4,30) ).getSteps().size();


        assertEquals(expectedListSize , actualListSize );
        assertEquals(expectedListSize2 , actualListSize2 );

    }
}
