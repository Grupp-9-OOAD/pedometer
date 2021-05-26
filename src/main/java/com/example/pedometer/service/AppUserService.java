package com.example.pedometer.service;

import com.example.pedometer.DTO.AppUserResponse;
import com.example.pedometer.DTO.TeamResponse;
import com.example.pedometer.model.AppUser;
import com.example.pedometer.model.Steps;
import com.example.pedometer.model.Team;
import com.example.pedometer.repository.AppUserRepository;
import com.example.pedometer.repository.StepsRepository;
import com.example.pedometer.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {

    private final AppUserRepository appUserRepository;

    private final StepsRepository stepsRepository;
    
    private final TeamRepository teamRepository;
    
    public List<AppUserResponse> getAll() {
        return appUserRepository.findAll().stream()
                .map(AppUser::toResponse)
                .collect(Collectors.toList());
    }

    public AppUserResponse addAppUser(AppUser appUser) {

        Optional<AppUser> existingAppUser = appUserRepository.findByEmail(appUser.getEmail());

        if (existingAppUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        } else {
            validateAppUser(appUser);
            return appUserRepository.save(appUser)
                    .toResponse();
        }
    }

    @Transactional
    public AppUserResponse updateUser(AppUser appUser) {

        Optional<AppUser> existingAppUser = appUserRepository.findByEmail(appUser.getEmail());

        if (existingAppUser.isPresent()) {
            existingAppUser.get()
                    .setFirstName(appUser.getFirstName())
                    .setLastName(appUser.getLastName());
            return appUserRepository.save(existingAppUser.get())
                    .toResponse();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not find user");
        }
    }

    public AppUserResponse updatePassword(String password, String email) {

        Optional<AppUser> existingAppUser = appUserRepository.findByEmail(email);

        if (existingAppUser.isPresent()) {
            existingAppUser.get()
                    .setPassword(password);
            return appUserRepository.save(existingAppUser.get())
                    .toResponse();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not find user");
        }
    }

    public String deleteAppUser(String email, String password) {

        appUserRepository.findByEmail(email)
                .ifPresentOrElse(appUser -> {
                    if (appUser.getPassword().equals(password)) {
                        appUserRepository.deleteById(appUser.getId());
                    }else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email does not exist");
                });

        return "User with email: " + email + " has been deleted";
    }
    
    public String removeFromTeam(String email, String teamName) {
        teamRepository.findByTeamName(teamName)
                .ifPresentOrElse(team -> {
                    List<AppUser> teamMembers = team.getTeamMembers();
                    teamMembers.stream()
                            .filter(member -> member.getEmail().equalsIgnoreCase(email))
                            .findFirst()
                            .ifPresentOrElse(member -> {
                                team.getTeamMembers().remove(member);
                                teamRepository.save(team.updateTotalSteps());
                            }, () -> {
                                throw new ResponseStatusException(HttpStatus.CONFLICT, "Member does not exist in team");
                            });
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Team name does not exist");
                });
        return String.format("User with email \"%s\" has been removed from team %s.", email, teamName);
    }

    public AppUserResponse validateLogin(String email, String password) {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not find user");
                });
        if (appUser.getPassword().equals(password)){

            return appUser.toResponse();
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    public AppUserResponse getAppUserByEmail(String email) {

        AppUser appUser = appUserRepository.findByEmail(email)
        .orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not find user");
        });

        return appUser.toResponse();

    }

    @Transactional
    public AppUserResponse addStepsToUser(AppUser user, int steps, LocalDate date) {
        AppUser appUser = appUserRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find user");
                });

        if (date == null) {
            date = LocalDate.now();
        }

        LocalDate finalDate = date;


        if (appUser.getSteps().stream()
                .anyMatch(steps1 -> steps1.getDate().equals(finalDate))) {
            appUser.getSteps().forEach(steps1 -> {
                if (steps1.getDate().equals(finalDate)) {
                    steps1.addToSteps(steps);
                }
            });
        } else {
            Steps newSteps = new Steps()
                    .setSteps(steps)
                    .setDate(finalDate);

            appUser.getSteps().add(stepsRepository.save(newSteps));
        }

        appUserRepository.save(appUser);

        getAllTeams(appUser.getEmail()).forEach(team -> teamRepository.save(team.updateTotalSteps()));

        return appUser.toResponse();
    }

    public Integer getStepsOfAppUser(String email) {

        Optional<AppUser> appUser = appUserRepository.findByEmail(email);

        if (appUser.isPresent()){
            return getSteps(appUser.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No such user found");
        }
    }

    private Integer getSteps(AppUser appUser){
        return appUser.getSteps().stream()
                .mapToInt(Steps::getSteps)
                .sum();
    }

    private void validateAppUser(AppUser appUser) {
        if (appUser.getEmail() == null || appUser.getFirstName() == null || appUser.getLastName() == null || appUser.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User need to have a firstname, lastname, email and password");
        }
    }


    public List<Team> getAllTeams(String email) {

        List<Team> allTeams = teamRepository.findAll();

        Optional<AppUser> appUser = appUserRepository.findByEmail(email);


        if (appUser.isPresent()){
            return allTeams.stream()
                    .filter(team -> team.getTeamMembers().contains(appUser.get()))
                    .collect(Collectors.toList());

        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user");
        }

    }
}
