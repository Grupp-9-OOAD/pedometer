package com.example.pedometer.model;

import com.example.pedometer.DTO.TeamResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.ArrayList;
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
    private Long totalSteps = 0L;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "team_members",
            joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(referencedColumnName = "id")})
    private List<AppUser> teamMembers = new ArrayList<>();

    public void addTeamMember(AppUser teamMember) {
        if(!teamMembers.contains(teamMember)) {
            teamMembers.add(teamMember);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already a member of this team");
    }

    public Team updateTotalSteps() {
        this.totalSteps = 0L;
        for (AppUser member: this.teamMembers) {
            this.totalSteps += member.getSteps().stream()
                    .mapToLong(Steps::getSteps)
                    .sum();
        }
        return this;
    }

    public TeamResponse toResponse(){
        return new TeamResponse()
                .setTeamMembers(this.getTeamMembers())
                .setTotalSteps(this.getTotalSteps())
                .setTeamName(this.getTeamName());
    }
}
