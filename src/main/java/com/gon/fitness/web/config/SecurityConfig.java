package com.gon.fitness.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests()
               .mvcMatchers("/","/login","/sign-up",
                       "/check-email","/check-email-token","/email-login","/check-email-login","/login-link"
                ,"/settings/**","/new-study","/study/**"
               )
               .permitAll()
               .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
               .anyRequest().authenticated();

       http.formLogin()
               .loginPage("/login").permitAll();

       http.logout()
               .logoutSuccessUrl("/");


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**","/favicon.ico")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        ;
    }



}
