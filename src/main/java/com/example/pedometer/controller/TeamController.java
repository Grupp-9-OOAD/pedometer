package com.example.pedometer.controller;

import com.example.pedometer.DTO.TeamResponse;
import com.example.pedometer.model.AppUser;
import com.example.pedometer.model.Team;
import com.example.pedometer.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<TeamResponse> addTeam(@RequestBody Team team){
        return ResponseEntity.ok(teamService.addTeam(team));
    }

    @PostMapping("/add/user")
    public ResponseEntity<TeamResponse> addUserToTeam(@RequestParam String userEmail,
                                                      @RequestParam String teamName){
        return ResponseEntity.ok(teamService.AddUserToTeam(userEmail, teamName));
    }
}
