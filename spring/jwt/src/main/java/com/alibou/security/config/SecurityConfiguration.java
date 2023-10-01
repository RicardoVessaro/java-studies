package com.alibou.security.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

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
                    /*
                     * Authorizing all the methods we have in the authorization controller.
                     * There we have only authentication methods, nothing related with business logic.
                     */
                    .requestMatchers("/api/v1/auth/**")
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
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> {
                /*
                 * In Spring, we don't need to provide a logout endpoint, when this url
                 *  is requested Spring will call the LogoutHandler.
                 */
                logout.logoutUrl("/api/v1/auth/logout");
                /*
                 * This is where we implement the logout mechanism.
                 */
                logout.addLogoutHandler(logoutHandler);
                /*
                 * Once the user logout we need to clear the context in order to the user have not access to the api.
                 */
                logout.logoutSuccessHandler(((request, response, authentication) ->
                    SecurityContextHolder.clearContext()));
            })
            ;


        return http.build();
    }

}
