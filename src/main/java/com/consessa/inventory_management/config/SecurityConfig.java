package com.consessa.inventory_management.config;

import com.consessa.inventory_management.model.AppUser;
import com.consessa.inventory_management.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUser = appUserRepository.findById(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
                return org.springframework.security.core.userdetails.User
                        .withUsername(appUser.getUsername())
                        .password(appUser.getPassword())
                        .roles(appUser.getRole())
                        .build();
            }
        }).passwordEncoder(passwordEncoder());
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder is deprecated but fine for offline assignment
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/index", "/login", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/assets", true)
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/?logout=true")
                .permitAll();

        http.headers().frameOptions().disable();
    }
}