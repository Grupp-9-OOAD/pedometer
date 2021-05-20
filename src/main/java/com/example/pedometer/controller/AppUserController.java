package com.example.pedometer.controller;

import com.example.pedometer.model.AppUser;
import com.example.pedometer.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllAppUsers() {
        return ResponseEntity.ok(appUserService.getAll());
    }

    @GetMapping("/get/one")
    public ResponseEntity<AppUser> getOneByEmail(@RequestParam  String email) {
        return ResponseEntity.ok(appUserService.getAppUserByEmail(email));
    }

    @PostMapping("/add")
    public ResponseEntity<AppUser> addAppUser(@RequestBody AppUser appUser) {
        return ResponseEntity.ok(appUserService.addAppUser(appUser));
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteAppUser(@RequestParam String email,
                                                @RequestParam String password) {
        return ResponseEntity.ok(appUserService.deleteAppUser(email, password));
    }

}