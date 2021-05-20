package com.example.pedometer.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Created by Patrik Melander
 * Date: 2021-05-20
 * Time: 14:15
 * Project: pedometer
 * Copyright: MIT
 */
@Getter
@Setter
@Accessors(chain = true)
public class StepsResponse {

    private LocalDate date;

    private int steps;
}
