package org.vromanyu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    @Profile("prod")
    public SecurityFilterChain prodFilterChain(HttpSecurity httpSecurity, BearerTokenLoggingFilter bearerTokenLoggingFilter) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api.gateway/*/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/api.gateway/swagger-ui/**").permitAll();
                    auth.requestMatchers("/api.gateway/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/api.gateway/v3/api-docs/swagger-config").permitAll();
                    auth.requestMatchers("/v3/api-docs/swagger-config").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .addFilterAfter(bearerTokenLoggingFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Profile("!prod")
    public SecurityFilterChain devFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.oauth2ResourceServer(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .build();
    }

}
