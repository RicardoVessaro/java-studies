package com.alibou.security.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

            /*
             * Disabling CSRF (We will talk about it in a different video) [Search for the video].
             */
            .csrf(csrf -> csrf.disable())
            /**
             * Whitelist endpoints are endpoints that don't require authentication like login and create account.
             * Authorizing any request in the Matchers
             */
            .authorizeHttpRequests(authHttp -> {
                authHttp
                    .requestMatchers("")
                    .permitAll()
                    /*
                     * Any other request should be authenticated.
                     */
                    .anyRequest()
                    .authenticated();
            })
            .sessionManagement(session -> {
                session
                    /*
                     * How we want to create our session.
                     * In this case stateless so every request is authenticated.
                     */
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authenticationProvider(authenticationProvider)
            /*
             * Calling our filter before the UsernamePasswordAuthenticationFilter.
             * Because we authenticate the user in the jwtAuthFilter.
             */
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
