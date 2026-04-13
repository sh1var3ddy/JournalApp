package com.example.journal.config;

import com.example.journal.service.AuthService;
import com.example.journal.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final AuthService authService;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(AuthService authService, JwtService jwtService, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = authService.loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new BadCredentialsException("Invalid JWT token");
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            SecurityContextHolder.clearContext();
            request.setAttribute("jwt_exception", ex);
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("JWT token has expired", ex)
            );
        } catch (JwtException | BadCredentialsException ex) {
            SecurityContextHolder.clearContext();
            request.setAttribute("jwt_exception", ex);
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("Invalid JWT token", ex)
            );
        }
    }
}