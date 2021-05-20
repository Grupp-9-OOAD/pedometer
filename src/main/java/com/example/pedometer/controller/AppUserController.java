package com.example.pedometer.controller;

import com.example.pedometer.DTO.AppUserResponse;
import com.example.pedometer.model.AppUser;
import com.example.pedometer.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getAllAppUsers() {
        return ResponseEntity.ok(appUserService.getAll());
    }

    @GetMapping("/get/one")
    public ResponseEntity<AppUserResponse> getOneByEmail(@RequestParam  String email) {
        return ResponseEntity.ok(appUserService.getAppUserByEmail(email));
    }

    @PostMapping("/add")
    public ResponseEntity<AppUserResponse> addAppUser(@RequestBody AppUser appUser) {
        return ResponseEntity.ok(appUserService.addAppUser(appUser));
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteAppUser(@RequestParam String email,
                                                @RequestParam String password) {
        return ResponseEntity.ok(appUserService.deleteAppUser(email, password));
    }

    @PostMapping("/add/steps")
    public ResponseEntity<AppUserResponse> addStepsToAppUser(@RequestBody AppUser appUser,
                                                     @RequestParam int steps,
                                                     @RequestParam @Nullable LocalDate date) {
        return ResponseEntity.ok(appUserService.addStepsToUser(appUser, steps, date));
    }

}
