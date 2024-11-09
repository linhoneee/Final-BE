package com.example.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final LoggingFilter loggingFilter;
//    private final RoleMatcher roleMatcher;

    public SecurityConfig(LoggingFilter loggingFilter) {
        this.loggingFilter = loggingFilter;
//        this.roleMatcher = roleMatcher;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String secret = "my_secret_key_which_should_be_very_long_and_secure";
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();

        return jwt -> decoder.decode(jwt)
                .doOnNext(decodedJwt -> {
                    // Log the JWT claims after decoding
                    System.out.println("Decoded JWT Claims: " + decodedJwt.getClaims());
                });
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Lấy giá trị `roles` từ claim, giả sử nó là chuỗi
            String role = (String) jwt.getClaims().get("roles");

            // Tạo `GrantedAuthority` từ vai trò duy nhất
            return List.of(new SimpleGrantedAuthority(role));
        });

        // Bọc JwtAuthenticationConverter trong ReactiveJwtAuthenticationConverterAdapter
        return new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter);
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(authorize -> {
                    authorize.anyExchange().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .addFilterAfter(
                        (exchange, chain) ->
                                loggingFilter.checkTokenAndRole(exchange)
                                        .then(chain.filter(exchange)),
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .build();
    }
}
