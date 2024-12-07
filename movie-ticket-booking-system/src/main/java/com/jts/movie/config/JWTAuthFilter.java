package com.jts.movie.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UserInfoUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Get the current request URI
		String requestURI = request.getRequestURI();

		// Bypass JWT authentication for specific public endpoints
		if (requestURI.startsWith("/user/register") || requestURI.startsWith("/user/login")) {
			// Proceed with the next filter without checking JWT token
			filterChain.doFilter(request, response);
			return;
		}

		// Get Authorization header and check if it contains a Bearer token
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		// Validate the header and extract the token
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // Extract JWT token from the Bearer string
			try {
				username = jwtService.extractUsername(token); // Extract the username from the token
			} catch (Exception e) {
				// Log or handle token parsing/validation errors
				System.out.println("JWT Token parsing failed: " + e.getMessage());
			}
		}

		// Check if username is valid and if authentication context is not already set
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// Validate the token against the user details
			if (jwtService.validateToken(token, userDetails)) {
				// Create UsernamePasswordAuthenticationToken with userDetails
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				// Set additional details such as the request information
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication context with the created token
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Proceed with the next filter in the chain
		filterChain.doFilter(request, response);
	}
}
