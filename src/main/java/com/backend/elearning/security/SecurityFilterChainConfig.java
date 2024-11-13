package com.backend.elearning.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final String ROLE_INSTRUCTOR = "INSTRUCTOR";
    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_STUDENT = "STUDENT";


    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider,
                                     JWTAuthenticationFilter jwtAuthenticationFilter,
                                     @Qualifier("delegatedAuthEntryPoint") AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((AbstractHttpConfigurer::disable))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                auth ->
                        auth
                                .requestMatchers("/api/v1/users/*",
                                        "/api/v1/admin/category/*",
                                        "/api/v1/admin/topics/*",
                                        "/api/v1/admin/coupons/*",
                                        "/api/v1/admin/reviews/*",
                                        "/api/v1/admin/students/*",
                                        "/api/v1/admin/orders/*",
                                        "/api/v1/admin/courses/*/status/*"
                                        ).hasRole(ROLE_ADMIN)
                                .requestMatchers(
                                        "/api/v1/admin/courses/*",
                                        "/api/v1/admin/lectures/*",
                                        "/api/v1/admin/quizzes/*",
                                        "/api/v1/statistic/*"
                                ).hasAnyRole(ROLE_INSTRUCTOR, ROLE_ADMIN)
                                .requestMatchers("/api/v1/payments/*",
                                        "/api/v1/orders",
                                        "/api/v1/orders/*/status/*",
                                        "/api/v1/orders/user",
                                        "/api/v1/notes/*",
                                        "/api/v1/learning-course/*",
                                        "/api/v1/learning-lectures/*",
                                        "/api/v1/learning-quizzes/*" ,
                                        "/api/v1/carts/*"
                                ).hasRole(ROLE_STUDENT)
                                .anyRequest().permitAll())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        return http.build();

    }
}
