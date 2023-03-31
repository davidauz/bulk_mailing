package com.davidauz.blkm_interface.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
//@EnableAsync  when and if we'll use threaded methods
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] STATIC_RESOURCES_ARRAY =
    {	"/login/**"
    ,	"/webjars/**"
    ,	"/auth/**"
    ,	"/do_login/**"
    ,	"/register/**"
    ,	"/css/**"
    ,	"/webfonts/**"
    ,	"/images/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(STATIC_RESOURCES_ARRAY).permitAll()
                        .requestMatchers("/users").hasRole("ADMIN")
                        .anyRequest().authenticated() // everything is secured by default in Spring but I am taking responsibility
                )
                .formLogin(form -> form
                                .loginPage("/login")
//				.loginProcessingUrl("/login") not necessary; it is the default
                                .defaultSuccessUrl("/dashboard")
                                .permitAll()
                ).logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
        ;
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
