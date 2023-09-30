package com.alibou.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * OncePerRequestFilter: A filter called in every request (Http Request).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    // @NonNull: These parameters should not be null.
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, // To access information about the request.
        @NonNull HttpServletResponse response, // To access and add information in the response.
        @NonNull FilterChain filterChain // Follows the filter chain pattern, it is used to call the next filter.
    ) throws ServletException, IOException {

    }
}
