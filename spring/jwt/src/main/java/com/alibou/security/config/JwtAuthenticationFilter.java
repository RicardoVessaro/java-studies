package com.alibou.security.config;

import com.alibou.security.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * OncePerRequestFilter: A filter called in every request (Http Request).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    /*
     * Spring has its own implementation for UserDetailsService, but we
     *  need to create our own implementation with our business rule.
     */
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    /*
     * @NonNull: These parameters should not be null.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, // To access information about the request.
        @NonNull HttpServletResponse response, // To access and add information in the response.
        @NonNull FilterChain filterChain // Follows the filter chain pattern, it is used to call the next filter.
    ) throws ServletException, IOException {
        /*
         *  When we make a call we need to pass the JWT authentication token within the header.
         *  request.getHeader("Authorization"): Gets the Authorization header.
         */
        final String authHeader = request.getHeader("Authorization");

        final String jwt;

        final String userEmail;

        /*
         * If there is no auth header or "Bearer " on in then do nothing.
         */
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Calling the next filter.
            return;
        }

        /*
         * Begins in 7 because "Bearear " has 8 characters
         */
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        /**
         * If the getAuthentication returns null means that the authentication wasn't done (The user is not connected yet).
         */
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Checking if the given token is valid
            var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // null because we don't create credentials
                    userDetails.getAuthorities()
                );
                /*
                 *  Adding more details, building details of the request.
                 */
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                /*
                 * Updating the SecurityContextHolder with the authentication
                 */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
