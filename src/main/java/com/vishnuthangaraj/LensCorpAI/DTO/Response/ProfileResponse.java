package com.vishnuthangaraj.LensCorpAI.DTO.Response;

import com.vishnuthangaraj.LensCorpAI.Enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    /*
        CLASS NAME  : ProfileResponse
        DESCRIPTION : This class functions as a Data Transfer Object (DTO) designed to encapsulate the result of
                      a successful operation of viewing the details of the currently logged-in user.
                      In particular, this class conveys information such as the user’s name, gender, email,
                      and password, facilitating communication between the server and client applications.
        ATTRIBUTES  : - `name` (String): Represents the full name of the user.
                      - `gender` (Gender): Enumerated type capturing the user's gender.
                      - `role` (Role): Enumerated type representing the user's role in the application (e.g., USER or ADMIN).
                      - `email` (String): Unique identifier for the user's account, used for authentication and communication.
                      - `password` (String): Securely stores the user's hashed password for authentication.
    */

    private String name;
    private Gender gender;
    private String email;
    private String password;
}
