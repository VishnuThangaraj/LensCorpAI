package com.vishnuthangaraj.LensCorpAI.Service;

import com.vishnuthangaraj.LensCorpAI.DTO.Response.ProfileResponse;
import com.vishnuthangaraj.LensCorpAI.Exceptions.AuthenticationExceptions.InvalidTokenException;
import com.vishnuthangaraj.LensCorpAI.Model.AppUser;
import com.vishnuthangaraj.LensCorpAI.Repository.AppUserRepository;
import com.vishnuthangaraj.LensCorpAI.Security.JwtBlacklistService;
import com.vishnuthangaraj.LensCorpAI.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {

    private  final AppUserRepository appUserRepository;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtService jwtService;

    /*
        SERVICE FUNCTION : getProfile
        DESCRIPTION      : This function fetches the token, verifies its validity and checks if it belongs to
                           the current user. If the token is valid, it provides the AppUser details to the
                           Profile Response DTO. If the token is invalid, it throws an InvalidTokenException.
        PARAMETER        : 'Null'
        RETURN           : On successful operation, it returns a Profile Response DTO containing AppUser details.
    */
    public ProfileResponse getProfile() throws InvalidTokenException {
        String currentUserToken = jwtService.getCurrentUserToken();

        // Check if the Provided Token is Valid
        if(currentUserToken == null || jwtBlacklistService.isTokenBlacklisted(currentUserToken)){
            log.warn("The user is attempting to view Profile without a Valid Token");
            throw new InvalidTokenException("No Token provided for Authentication");
        }

        String userEmail = jwtService.extractUserName(currentUserToken); // Extract Current User Email
        AppUser currentUser = appUserRepository.getUserByEmail(userEmail); // Get Current User By Email

        return ProfileResponse.builder()
                .name(currentUser.getName())
                .gender(currentUser.getGender())
                .email(currentUser.getEmail())
                .password(currentUser.getPassword())
                .build();
    }
}
