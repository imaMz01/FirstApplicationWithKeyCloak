package com.Peaqock.FirstAppWithKeyCloak.Config;


import com.Peaqock.FirstAppWithKeyCloak.Converter.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthConverter jwtAuthConverter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        httpSecurity
                .csrf( csrf -> csrf.disable())
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated());

        httpSecurity
                .oauth2ResourceServer(oauth2 -> oauth2 // It allows the application to accept and validate JWT tokens sent in requests with OAuth2 Resource Server
                        .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter)) //we mention to spring that we want to use our converter instead to the default converter
                );

        httpSecurity
                .sessionManagement(oauth -> oauth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
