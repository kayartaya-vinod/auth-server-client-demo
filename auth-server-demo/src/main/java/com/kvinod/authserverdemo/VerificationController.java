package com.kvinod.authserverdemo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class VerificationController {
    @Value("${secret_key}")
    String secretKey;

    @GetMapping(value = "/verify")
    public Map<String, Object> verifyJwt(@RequestParam String jwt) throws JsonProcessingException {
        Map<String, Claim> claims;
        log.info(">>> secretKey is {}", secretKey);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        claims = decodedJWT.getClaims();
        log.info(">>> in VerificationController.verifyJwt, claims = {}", claims);
        Map<String, Object> expectedMap = new HashMap<>();
        for (Entry<String, Claim> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue().as(String.class));
        }
        log.info(">>> in VerificationController.verifyJwt, expectedMap = {}", expectedMap);
        return expectedMap;
    }

}
