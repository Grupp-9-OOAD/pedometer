package com.example.pedometer.service;

import com.example.pedometer.DTO.TeamResponse;
import com.example.pedometer.model.Team;
import com.example.pedometer.repository.AppUserRepository;
import com.example.pedometer.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final AppUserRepository appUserRepository;

    public List<TeamResponse> getAll() {
        return teamRepository.findAll().stream()
                .map(Team::toResponse)
                .collect(Collectors.toList());
    }

    public TeamResponse addTeam(Team team) {
        validateTeam(team);
        return teamRepository.save(team)
                .toResponse();
    }

    public TeamResponse addUserToTeam(String userEmail, String teamName) {

        Team currentTeam = teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find team");
                });

        appUserRepository.findByEmail(userEmail)
                        .ifPresent(appUser -> {
                            currentTeam.addTeamMember(appUser);
                            teamRepository.save(currentTeam.updateTotalSteps());
                        });
        return currentTeam
                .toResponse();
    }

    private void validateTeam(Team team) {
        if (team.getTeamName() == null || team.getTeamName().isBlank()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Team name is missing");
        }

        teamRepository.findByTeamName(team.getTeamName())
                .ifPresent(team1 -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Team name is taken");
                });

    }


    public HttpStatus deleteTeam(Team team) {

        Optional<Team> teamToDelete = teamRepository.findByTeamName(team.getTeamName());


        if(teamToDelete.isPresent()){
            if (teamToDelete.get().getTeamMembers().isEmpty()){
                teamRepository.delete(teamToDelete.get());
                return HttpStatus.OK;
            }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not delete team with members");


        }else throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not find team");

    }

}
