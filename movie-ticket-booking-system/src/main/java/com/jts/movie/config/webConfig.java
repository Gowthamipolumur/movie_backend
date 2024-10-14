package com.jts.movie.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Apply CORS to all endpoints (use "/**" for all endpoints)
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200", "http://localhost:8080") // Allow specific origins (adjust based on where frontend is running)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow sending credentials (optional)
            }
        };
    }
}


