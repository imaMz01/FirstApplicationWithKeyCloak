package com.Peaqock.FirstAppWithKeyCloak.Converter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
//we convert, because role should have a prefix "Role_", so we should add "Role_"
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();
    @Value("${jwt.auth.converter.principle-attribute}")
    private String pricipleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {

        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractRessourceRoles(jwt).stream()
        )
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt,authorities,getPrincipalClaimName(jwt));
    }

    public String getPrincipalClaimName(Jwt jwt){
        String claimName = JwtClaimNames.SUB;
        if(pricipleAttribute != null){
            claimName=pricipleAttribute;
        }
        return jwt.getClaim(claimName);
    }

    public Collection< ? extends GrantedAuthority> extractRessourceRoles(Jwt jwt){
        Map<String,Object> ressoursAccess;
        Map<String,Object> ressource;
        Collection<String> ressourceRole;

        if(jwt.getClaim("resource_access") == null){
            return Set.of();
        }
        ressoursAccess=jwt.getClaim("resource_access");

        if(ressoursAccess.get(resourceId) == null){
            return Set.of();
        }
        ressource= (Map<String, Object>) ressoursAccess.get(resourceId);
        ressourceRole= (Collection<String>) ressource.get(("roles"));
        return ressourceRole.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toSet());
    }
}
