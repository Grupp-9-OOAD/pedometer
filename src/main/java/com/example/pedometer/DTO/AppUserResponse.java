package com.example.pedometer.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class AppUserResponse {

    private String firstName;

    private String lastName;

    private String password;

    private String email;

}
