package com.bilgeadam.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.PostManagerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenProvider {
    @Value("${secretkey}")
    String secretKey;
    @Value("${audience}")
    String audience;
    @Value("${issuer}")
    String issuer;

    public Optional<Long> getIdFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null){
                throw new PostManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id); // == Optional<Long>
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
    }

}
