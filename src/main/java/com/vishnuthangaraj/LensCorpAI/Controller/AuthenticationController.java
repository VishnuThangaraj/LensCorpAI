package com.vishnuthangaraj.LensCorpAI.Controller;


import com.vishnuthangaraj.LensCorpAI.DTO.Request.AuthenticationRequest;
import com.vishnuthangaraj.LensCorpAI.DTO.Request.RegistrationRequest;
import com.vishnuthangaraj.LensCorpAI.DTO.Response.AuthenticationResponse;
import com.vishnuthangaraj.LensCorpAI.DTO.Response.GeneralResponse;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.DuplicateEmailException;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.InvalidLoginException;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.InvalidTokenException;
import com.vishnuthangaraj.LensCorpAI.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Authentication Controller", description = "This controller encompasses endpoints related to user authentication.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    //URL : http://localhost:8081/api/auth/register
    @Operation(
            summary = "Handles HTTP POST requests for New-User registration.",
            description = "This invokes the Authentication service to handle the Registration request," +
                    "Utilizing the user details provided via the registration request DTO." +
                    "No-Token Required for registration",
            tags = {"Registration", "POST", "AppUser"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New User Successfully Registered to Database, Returns JWT Token on successful Registration",
                    content = {@Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "Application/json")}),
            @ApiResponse(responseCode = "409", description = "Email Already exists in Database",
                    content = {@Content(schema = @Schema(implementation = DuplicateEmailException.class), mediaType = "Application/json")})

    })
    @Parameters({
            @Parameter(name = "Registration Request", description = "The RegistrationRequest DTO should include the {Name, Gender, Email, and Password}.", required = true)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> userRegistration(
            @RequestBody RegistrationRequest request
    ){
        return ResponseEntity.ok(authService.userRegistration(request));
    }


    // URL : http://localhost:8081/api/auth/authenticate
    @Operation(
            summary = "Handles User login process by validating the provided credentials.",
            description = "This function necessitates a JSON Object containing of Email and Password through Request Body.\n" +
                    "If the credentials are valid, generates a JWT token and sends it in the Response." +
                    "If the credentials are invalid, returns a response with an appropriate HTTP status."+
                    "JWT-Token Required for Authentication",
            tags = {"Authentication", "POST", "AppUser"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Existing User Authenticated Successfully, Returns JWT Token on successful Registration",
                    content = {@Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "Application/json")}),
            @ApiResponse(responseCode = "401", description = "Invalid Login Details Provided",
                    content = {@Content(schema = @Schema(implementation = InvalidLoginException.class), mediaType = "Application/json")})

    })
    @Parameters({
            @Parameter(name = "Authentication Request", description = "The AuthenticationRequest DTO should include the {Email, and Password}.", required = true)
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> userAuthentication(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authService.authenticateAndLogin(request));
    }

    // http://localhost:8081/api/auth/logout
    @Operation(
            summary = "Manages the user logout procedure by verifying the JWT Token.",
            description = "Retrieve the provided JWT token and log out the currently logged-in user. " +
                    "If the token is valid, the user will be successfully logged out" +
                    "If the token is invalid, an Exception will be thrown.",
            tags = {"Logout", "POST", "AppUser"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The User who was signed in has Successfully Logged-Out.",
                    content = {@Content(schema = @Schema(implementation = GeneralResponse.class), mediaType = "Application/json")}),
            @ApiResponse(responseCode = "401", description = "The provided token for the logout process is invalid.",
                    content = {@Content(schema = @Schema(implementation = InvalidTokenException.class), mediaType = "Application/json")})

    })
    @PostMapping("logout")
    public ResponseEntity<GeneralResponse> userLogout() throws InvalidTokenException {
        return ResponseEntity.ok(authService.userLogout());
    }
}
