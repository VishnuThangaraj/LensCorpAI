package com.vishnuthangaraj.LensCorpAI.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {

    /*
        CLASS NAME  : GeneralResponse
        DESCRIPTION : This class acts as a Data Transfer Object (DTO), specifically designed to encapsulate
                      messages related to operations performed within the application.
                      It facilitates communication between the server and client applications
                      and includes an attribute named ‘message’.
        ATTRIBUTES  : String (message)
    */

    private String message;
}
