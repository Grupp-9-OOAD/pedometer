package com.example.pedometer.controller;

import com.example.pedometer.model.Steps;
import com.example.pedometer.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/steps")
public class StepsController {

    private final StepService stepService;

    @PostMapping("/add")
    public ResponseEntity<Steps> addSteps(@RequestBody int steps, LocalDate date){
        return ResponseEntity.ok(stepService.addSteps(steps, date));
    }
}
