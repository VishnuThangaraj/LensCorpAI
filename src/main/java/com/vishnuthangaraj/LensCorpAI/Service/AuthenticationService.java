package com.vishnuthangaraj.LensCorpAI.Service;

import com.vishnuthangaraj.LensCorpAI.DTO.Request.AuthenticationRequest;
import com.vishnuthangaraj.LensCorpAI.DTO.Request.RegistrationRequest;
import com.vishnuthangaraj.LensCorpAI.DTO.Response.AuthenticationResponse;
import com.vishnuthangaraj.LensCorpAI.DTO.Response.GeneralResponse;
import com.vishnuthangaraj.LensCorpAI.Enums.Role;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.DuplicateEmailException;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.InvalidLoginException;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.InvalidTokenException;
import com.vishnuthangaraj.LensCorpAI.Model.AppUser;
import com.vishnuthangaraj.LensCorpAI.Repository.AppUserRepository;
import com.vishnuthangaraj.LensCorpAI.Security.JwtBlacklistService;
import com.vishnuthangaraj.LensCorpAI.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private  final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final JwtBlacklistService jwtBlacklistService;

    /*
        SERVICE FUNCTION : userRegistration
        DESCRIPTION      : This function is responsible for checking the existence of an email in the system.
                           If the email is not already associated with an existing user account,
                           it proceeds to create a new user account with the provided details.
        PARAMETER        : RegistrationRequest {name, gender, email, password}
        RETURN           : Returns JwtToken if Registration is Successful
    */
    public AuthenticationResponse userRegistration(RegistrationRequest request) {

        // Check if the Email Already Exists
        if(appUserRepository.findByEmail(request.getEmail()).isPresent()){
            log.warn("User Provided Duplicate Email for Registration");
            throw new DuplicateEmailException(request.getEmail());
        }

        var enrolledUser = AppUser.builder() // Register New User
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .gender(request.getGender())
                .build();

        appUserRepository.save(enrolledUser);

        log.info("New User Registration Completed");

        // Generate Token and return Authentication Response
        var jwtToken = jwtService.generateToken(enrolledUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /*
        SERVICE FUNCTION : authenticateAndLogin
        DESCRIPTION      : This service function is designed to authenticate a user by verifying their
                           credentials against the stored information in the system.
                           Upon successful authentication, it performs the login operation, generating
                           and returning an authentication token for the user's session.
        PARAMETER        : AuthenticationRequest {(String) email , (String) password}
        RETURN           : Returns JwtToken if Authentication is Successful, else throws InvalidLoginException
    */
    public AuthenticationResponse authenticateAndLogin(AuthenticationRequest request){
        // Validate the Credentials
        var user = appUserRepository.findByEmail(request.getEmail())
                .orElse(null);
        if(user == null){
            log.warn("User with this Email does not exists");
            throw new InvalidLoginException();
        }

        // Validate the Password
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        log.info("User Login Successful");

        // Generate Token and return Authentication Response
        var jwtToken = jwtService.generateToken(user);
        log.info("Token Generated Successfully");

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /*
        SERVICE FUNCTION : userLogout
        DESCRIPTION      : This function checks the provided token, logs out the currently logged-in user,
                           and adds the token to a blacklist to prevent multiple logouts
        PARAMETER        : 'Null'
        RETURN           : Returns GeneralResponse if Registration is Successful
    */
    public GeneralResponse userLogout() throws InvalidTokenException {
        String currentUserToken = jwtService.getCurrentUserToken();

        if(currentUserToken == null){
            log.warn("The user is attempting to Logout without a Valid Token");
            throw new InvalidTokenException("No Token provided for Authentication");
        }

        // Validate Token
        if(jwtBlacklistService.isTokenBlacklisted(currentUserToken)){
            log.warn("The User is attempting to Logout without a Valid Token");
            throw new InvalidTokenException("A token that has already expired has been supplied for the logout operation, " +
                    "or the user has previously logged out.");
        }

        String userEmail = jwtService.extractUserName(currentUserToken); // Extract Current User Email

        jwtBlacklistService.blacklistToken(currentUserToken); // Prevents Multiple Logout of Same User
        log.info("User Logged-Out Successfully");
        return new GeneralResponse("User with ID "+ userEmail +" Logout Successful");
    }
}
