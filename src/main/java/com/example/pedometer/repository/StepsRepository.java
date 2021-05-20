package com.example.pedometer.repository;

import com.example.pedometer.model.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-05-20
 * Time: 13:43
 * Project: pedometer
 * Copyright: MIT
 */
@Repository
public interface StepsRepository extends JpaRepository<Steps, UUID> {
}
