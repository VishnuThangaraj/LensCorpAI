package com.vishnuthangaraj.LensCorpAI.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /*
        FUNCTION NAME : doFilterInternal
        DESCRIPTION : This method is an overridden method from the OncePerRequestFilter class,
                      and it serves as a key component of the authentication and authorization process
                      in a Spring Security filter chain. This method intercepts incoming HTTP requests,
                      extracts and validates JWTs from the Authorization header,
                      and updates the security context if the token is valid.
        PARAMETER : HttpServletRequest (request), HttpServletResponse (response), FilterChain (filterChain)
    */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String JwtToken, userEmail;

        // Validate AuthHeader
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        JwtToken = authHeader.substring(7); // Excluding bearer Substring (ignore = "Bearer ")
        userEmail = jwtService.extractUserName(JwtToken);// Extract the email from the JWT token

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Get user from database(check if user exits)
            UserDetails appUser = this.userDetailsService.loadUserByUsername(userEmail);

            if(jwtService.isTokenValid(JwtToken, appUser)){
                // To update security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        appUser,
                        JwtToken,
                        appUser.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
