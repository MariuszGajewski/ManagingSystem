package com.MG.ManagingSystem;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/*")
                .authenticated()
                .antMatchers("/resources/**").permitAll().and()
                .formLogin()
                .loginPage("/loginPage").permitAll()
                .usernameParameter("userName")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
                .failureUrl("/loginPage?error=true")
                .and()
                .logout().logoutSuccessUrl("/loginPage");
    }
    /*@Override
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
                //User.withDefaultPasswordEncoder()
                        //.username("admin")
                        .password("faust")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(Collections.singletonList(user));
    }*/
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("faust").roles("ADMIN");
    }

}
