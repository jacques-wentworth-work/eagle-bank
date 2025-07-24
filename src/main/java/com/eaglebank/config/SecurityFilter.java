package com.eaglebank.config;

import com.eaglebank.entity.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {

    public static final String PATH_V1_USERS = "/v1/users";
    public static final String PATH_V1_USERS_ID = "/v1/users/**";
    public static final String PATH_V1_ACCOUNTS_ID = "/v1/accounts/**";
    public static final String PATH_V1_AUTHENTICATE = "/v1/authenticate";

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/h2-console/**").permitAll();
                    auth.requestMatchers("/error").permitAll();

                    auth.requestMatchers(HttpMethod.POST, PATH_V1_AUTHENTICATE).permitAll();

                    //User
                    auth.requestMatchers(HttpMethod.POST, PATH_V1_USERS).permitAll();
                    auth.requestMatchers(HttpMethod.GET, PATH_V1_USERS_ID).hasAuthority(Permission.USER.name());
                    auth.requestMatchers(HttpMethod.PATCH, PATH_V1_USERS_ID).hasAuthority(Permission.USER.name());
                    auth.requestMatchers(HttpMethod.DELETE, PATH_V1_USERS_ID).hasAuthority(Permission.USER.name());

                    //Accounts & transactions
                    auth.requestMatchers(PATH_V1_ACCOUNTS_ID).hasAuthority(Permission.USER.name());

                    auth.anyRequest().denyAll();
                })
        ;

        return http.build();
    }
}
