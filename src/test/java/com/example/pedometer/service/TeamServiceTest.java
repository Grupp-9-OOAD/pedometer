package com.example.pedometer.service;

import com.example.pedometer.model.AppUser;
import com.example.pedometer.model.Team;
import com.example.pedometer.repository.AppUserRepository;
import com.example.pedometer.repository.StepsRepository;
import com.example.pedometer.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    
    @Mock
    AppUserRepository mockAppUserRepository;
    
    @Mock
    TeamRepository mockTeamRepository;
    
    TeamService teamService;
    
    List<AppUser> mockList;
    
    AppUser mockUser1;
    AppUser mockUser2;
    AppUser mockUser3;
    
    Team mockTeam;
    
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
        
        mockTeam = new Team()
                .setTeamName("Mocked")
                .setTeamMembers(mockedMembers);
        
        teamService = new TeamService(mockTeamRepository, mockAppUserRepository);
    }
    
    @Test
    void addUserToTeamTest() {
        when(mockTeamRepository.findByTeamName(mockTeam.getTeamName()))
                .thenReturn(java.util.Optional.ofNullable(mockTeam));
        when(mockAppUserRepository.findByEmail(mockUser3.getEmail()))
                .thenReturn(java.util.Optional.ofNullable(mockUser3));
    
        assertEquals(2, mockTeamRepository.findByTeamName(mockTeam.getTeamName()).get().getTeamMembers().size());
        teamService.addUserToTeam(mockUser3.getEmail(), mockTeam.getTeamName());
        assertEquals(3, mockTeamRepository.findByTeamName(mockTeam.getTeamName()).get().getTeamMembers().size());
        
        verify(mockTeamRepository, times(3)).findByTeamName(anyString());
        verify(mockTeamRepository, times(1)).save(any());
    }
}
