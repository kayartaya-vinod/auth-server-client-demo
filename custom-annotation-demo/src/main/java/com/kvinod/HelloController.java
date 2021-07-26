package com.kvinod;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {

    @GetMapping("/public/welcome")
    public String sayWelcome(@RequestParam String name) {
        return String.format("Welcome back, %s!", name);
    }

    @GetMapping("/private/hello")
    public ResponseEntity<String> sayHello(@RequestParam String name,
            @RequestAttribute(required = false, name = "claims") Map<String, Object> claims)
            throws AccessDeniedException {

        if (claims == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization header may be missing or invalid.");
        }

        log.info("Saying hello to {}", name);
        log.info(">>> in HelloController.sayHello, claims = {}", claims);
        String out = String.format("Hello, %s! This is the content of claims: %s.", name, claims);
        return ResponseEntity.ok(out);
    }

}
