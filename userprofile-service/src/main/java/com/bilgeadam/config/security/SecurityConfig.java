package com.bilgeadam.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    JwtFilter getJwtTokenFilter(){
        return new JwtFilter();
    }

    //TODO endpoinler düzenlenecek
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable();

        httpSecurity.authorizeRequests()
                .antMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(getJwtTokenFilter(),UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
