package com.vishnuthangaraj.LensCorpAI.Controller;

import com.vishnuthangaraj.LensCorpAI.DTO.Response.ProfileResponse;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.InvalidTokenException;
import com.vishnuthangaraj.LensCorpAI.Service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Application Controller", description = "This controller encompasses endpoints related to user Data fetching.")
@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // http://localhost:8081/api/app/profile
    @Operation(
            summary = "This Verify the JWT token to view the details of the currently Logged-In User",
            description = "Retrieve the provided JWT token and fetch the profile of the currently logged-in user." +
                    "If the token is valid, the user’s profile will be presented via the profile response DTO." +
                    "If the token is invalid, an Exception will be thrown.",
            tags = {"Profile", "GET", "AppUser"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The details of the signed-in user have been successfully displayed.",
                    content = {@Content(schema = @Schema(implementation = ProfileResponse.class), mediaType = "Application/json")}),
            @ApiResponse(responseCode = "401", description = "The token provided for viewing the currently logged-in profile is invalid.",
                    content = {@Content(schema = @Schema(implementation = InvalidTokenException.class), mediaType = "Application/json")})

    })
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getCurrentUserProfile() throws InvalidTokenException {
        return ResponseEntity.ok(applicationService.getProfile());
    }
}
