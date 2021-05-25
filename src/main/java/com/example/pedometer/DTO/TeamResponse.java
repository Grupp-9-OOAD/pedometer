package com.example.pedometer.DTO;

import com.example.pedometer.model.AppUser;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.OneToMany;
import java.util.List;

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
public class TeamResponse {

    private String teamName;

    private Long totalSteps;

    private List<AppUser> teamMembers;
}
