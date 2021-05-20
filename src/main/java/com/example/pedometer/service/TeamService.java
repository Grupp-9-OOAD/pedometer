package com.example.pedometer.service;

import com.example.pedometer.DTO.TeamResponse;
import com.example.pedometer.model.Team;
import com.example.pedometer.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<TeamResponse> getAll() {
        return teamRepository.findAll().stream().map(Team::toResponse).collect(Collectors.toList());
    }
}
