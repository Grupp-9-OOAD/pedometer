package com.example.pedometer.service;

import com.example.pedometer.DTO.AppUserResponse;
import com.example.pedometer.model.AppUser;
import com.example.pedometer.model.Steps;
import com.example.pedometer.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

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

    public String deleteAppUser(String email, String password) {

        appUserRepository.findByEmail(email)
                .ifPresentOrElse(appUser -> {
                    if (appUser.getPassword().equals(password)) {
                        appUserRepository.deleteById(appUser.getId());
                    }
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "email does not exist");
                });

        return "User with email: " + email + " has been deleted";
    }

    public AppUserResponse getAppUserByEmail(String email) {

        AppUser appUser = appUserRepository.findByEmail(email)
        .orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no user found");
        });

        return appUser.toResponse();

    }

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
            appUser.getSteps().add(
                    new Steps()
                            .setSteps(steps)
                            .setDate(finalDate));
        }

        return appUserRepository.save(appUser)
                .toResponse();
    }

    private void validateAppUser(AppUser appUser) {
        if (appUser.getEmail() == null || appUser.getFirstName() == null || appUser.getLastName() == null || appUser.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "user need to have a firstname, lastname, email and password");
        }
    }
}
