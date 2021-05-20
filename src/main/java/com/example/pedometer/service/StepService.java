package com.example.pedometer.service;

import com.example.pedometer.repository.StepsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by Patrik Melander
 * Date: 2021-05-20
 * Time: 13:53
 * Project: pedometer
 * Copyright: MIT
 */
@Service
@RequiredArgsConstructor
public class StepService {
    private final StepsRepository stepsRepository;


    public Object addSteps(int steps, LocalDate date) {

    }
}
