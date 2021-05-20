package com.example.pedometer.repository;

import com.example.pedometer.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-05-20
 * Time: 13:41
 * Project: pedometer
 * Copyright: MIT
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
}
