package com.example.pedometer.service;

import com.example.pedometer.DTO.AppUserResponse;
import com.example.pedometer.model.AppUser;
import com.example.pedometer.model.Steps;
import com.example.pedometer.repository.AppUserRepository;
import com.example.pedometer.repository.StepsRepository;
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

    public List<AppUserResponse> getAll() {
        return appUserRepository.findAll().stream()
                .map(AppUser::toResponse)
                .collect(Collectors.toList());
    }

    public AppUserResponse addAppUser(AppUser appUser) {

        Optional<AppUser> existingAppUser = appUserRepository.findByEmail(appUser.getEmail());

        if (existingAppUser.isPresent()) {
            existingAppUser.get()
                    .setFirstName(appUser.getFirstName())
                    .setLastName(appUser.getLastName())
                    .setPassword(appUser.getPassword());
            return appUserRepository.save(existingAppUser.get())
                    .toResponse();
        } else {
            validateAppUser(appUser);
            return appUserRepository.save(appUser)
                    .toResponse();
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
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no user found");
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
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "email does not exist");
                });

        return "User with email: " + email + " has been deleted";
    }

    public AppUserResponse validateLogin(String email, String password) {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no user found");
                });

       log.info(">> appuser : {}", appUser);
        if (appUser.getPassword().equals(password)){

            return appUser.toResponse();
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    public AppUserResponse getAppUserByEmail(String email) {

        AppUser appUser = appUserRepository.findByEmail(email)
        .orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no user found");
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

        return appUserRepository.save(appUser)
                .toResponse();
    }

    public Integer getStepsOfAppUser(String email) {

        Optional<AppUser> appUser = appUserRepository.findByEmail(email);

        if (appUser.isPresent()){
            return getSteps(appUser.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no user found");
        }

    }

    private Integer getSteps(AppUser appUser){
        return appUser.getSteps().stream()
                .mapToInt(Steps::getSteps)
                .sum();
    }

    private void validateAppUser(AppUser appUser) {
        if (appUser.getEmail() == null || appUser.getFirstName() == null || appUser.getLastName() == null || appUser.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "user need to have a firstname, lastname, email and password");
        }
    }



}
