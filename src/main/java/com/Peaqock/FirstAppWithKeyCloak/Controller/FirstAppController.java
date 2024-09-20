package com.Peaqock.FirstAppWithKeyCloak.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keyCloak")
public class FirstAppController {

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public String hello1(){
        return "Hello from Spring Boot & KeyCloak";
    }

    @GetMapping("/hello2")
    @PreAuthorize("hasRole('admin')")
    public String hello2(){
        return "Hello from Spring Boot & KeyCloak - ADMIN";
    }
}
