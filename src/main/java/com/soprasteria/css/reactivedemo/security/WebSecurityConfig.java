package com.soprasteria.css.reactivedemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig implements WebFluxConfigurer {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                // ...
                .authorizeExchange( exchanges ->
                        exchanges
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .pathMatchers("/countdown/**").permitAll()
                                .anyExchange().authenticated()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .headers(ServerHttpSecurity.HeaderSpec::disable)
                .httpBasic(Customizer.withDefaults());
                // ...
        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        List<UserDetails> userDetails = List.of("user", "admin").stream()
                .map(username ->
                        User.builder()
                                .username(username)
                                .password(username)
                                .roles("USER")
                                .build()
                ).toList();
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    @ApplicationScope
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(List.of("https://example.com"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("*"));

        registry.addMapping("/**")
                .combine(configuration);
//      alternative set it direct
//                .allowedMethods("GET","POST")
//                .allowedOrigins("https://example.com")
//                .allowedHeaders("*")
//                .allowCredentials(true);
    }
}
