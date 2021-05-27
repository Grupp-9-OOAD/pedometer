package com.example.pedometer.repository;

import com.example.pedometer.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-05-20
 * Time: 13:45
 * Project: pedometer
 * Copyright: MIT
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    Optional<Team> findByTeamName(String teamName);
    boolean existsByTeamName(String teamName);

}
