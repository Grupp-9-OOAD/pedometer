package com.example.pedometer.model;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
public class Steps {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate date;
    private int steps;

    public void addToSteps(int newSteps) {
        this.steps += newSteps;
    }

}
