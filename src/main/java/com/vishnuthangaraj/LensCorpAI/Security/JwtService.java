package com.vishnuthangaraj.LensCorpAI.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    /*
        Hex Key        : 7a55ac2c64b6870c2e2bf8337df67f17ad1ed9c827c1c573b04793f94c885959
        Plain text key : EDGE LESS COD OSLO COAT FRED OAT BULB CUT WIND CLOT GILT LENT WALT DEFY
                         AVID HUE MEEK LION SIT WRIT HOBO FOUL LENS
    */
    private static final String SECRET_KEY = "7a55ac2c64b6870c2e2bf8337df67f17ad1ed9c827c1c573b04793f94c885959";

    /*
        FUNCTION NAME : extractUserName
        DESCRIPTION   : This method is entrusted with the task of extracting the username from the
                        supplied JWT token. It serves to authenticate whether the presented token indeed
                        corresponds to the user in Process.
        PARAMETER     : String (token) {JwtToken}
    */
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject); // sub = email/username
    }

    /*
        FUNCTION NAME : extractClaim
        DESCRIPTION   : This method facilitates the extraction of a specific claim from a JWT (JSON Web Token)
                        by utilizing a provided Function<Claims, T> to resolve the desired claim value.
                        JWTs typically contain claims as key-value pairs that convey information about the
                        token's subject, expiration, issuer, and additional custom attributes.
        PARAMETER     : String (token) {JwtToken}, Function<Claims, T> claimsResolver
    */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*
        FUNCTION NAME : generateToken
        DESCRIPTION   : This method is designed to generate a JWT (JSON Web Token) without including any
                        extra claims beyond the standard claims derived from the provided UserDetails object.
                        JWTs commonly include claims such as the subject, expiration, issuer, and user roles.
                        This variant of the method provides a simplified way to generate a token without
                        additional custom claims.
        PARAMETER     : UserDetails (userDetails)
    */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /*
        FUNCTION NAME : generateToken (Function Overloading)
        DESCRIPTION   : This method is responsible for creating a JSON Web Token (JWT) by combining
                        standard and custom claims. It leverages the JWTS builder provided by the JJWT
                        library to construct the token with user-related details and additional
                        custom claims if specified.
        PARAMETER     : Map<String, Object> (extraClaims), UserDetails (userDetails)
    */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +1000 * 60 * 24)) // 1day 1000 milsec
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /*
        FUNCTION NAME : extractAllClaims
        DESCRIPTION   : This method is responsible for decoding and extracting all claims from a JSON
                        Web Token (JWT). This private method utilizes the JJWT library
                        to parse a JWT, verify its signature using the signing key,
                        and retrieve the claims stored in the token's payload.
        PARAMETER     : String (token)
    */

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*
        FUNCTION NAME : isTokenValid
        DESCRIPTION   : This method determines the validity of a JSON Web Token (JWT) by comparing the
                        username extracted from the token with the username obtained from
                        the provided UserDetails. Additionally, it checks whether the token
                        has expired using the isTokenExpired method.
        PARAMETER     : String (token), UserDetails (userDetails)
    */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /*
        FUNCTION NAME : isTokenExpired
        DESCRIPTION   : This method assesses whether a JSON Web Token (JWT) has expired by comparing its
                        expiration date with the current date and time. This private method is commonly
                        used as part of token validation to ensure that the token is still within
                        its designated validity period.
        PARAMETER     : String (token)
    */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*
        FUNCTION NAME : extractExpiration
        DESCRIPTION   : This method is responsible for extracting the expiration date from a JSON
                        Web Token (JWT). This private method utilizes a generic claim extraction mechanism,
                        allowing for the retrieval of various claims from the token payload.
                        In this specific case, it extracts the expiration date using the Claims::getExpiration
        PARAMETER     : String (token)
    */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
        FUNCTION NAME : getSignInKey
        DESCRIPTION   : This method is responsible for obtaining the signing key used in the generation
                        and validation of JSON Web Tokens (JWTs). This private method converts a
                        Base64-encoded secret key (SECRET_KEY) into a cryptographic key suitable for
                        HMAC (Hash-based Message Authentication Code) operations.
        PARAMETER     : 'null'
    */
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
        FUNCTION NAME : getCurrentUserToken
        DESCRIPTION   : This method is responsible for retrieving the authentication token of the current
                        user from the Spring Security context. This token serves as a secure identifier
                        associated with the authenticated user and is commonly used for authorization purposes.
        PARAMETER     : 'null'
    */
    public String getCurrentUserToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null ? authentication.getCredentials().toString() : null;

    }
}


