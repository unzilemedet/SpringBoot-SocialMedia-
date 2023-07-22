package com.bilgeadam.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
public class SecurityConfig { // WebSecurityConfigurerAdapter ==> spring 5.0' dan beri kullanılmıyor{

    @Bean
    JwtFilter getJwtTokenFilter(){
        return new JwtFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/api/v1/auth/login",
                        "/api/v1/auth/loginMd5",
                        "/api/v1/auth/register",
                        "/api/v1/auth/registermd5",
                        "/api/v1/auth/activate-status",
                        "/api/v1/auth/forgot-password",
                        "/api/v1/auth/forgot-password-send-mail-token").permitAll().anyRequest().authenticated();

        httpSecurity.addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
