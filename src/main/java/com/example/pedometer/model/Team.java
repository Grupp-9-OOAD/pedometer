package com.example.pedometer.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
public class Team {

    @Id
    @GeneratedValue
    private UUID id;

    private String teamName;

    @OneToMany
    private List<AppUser> teamMembers;
}
