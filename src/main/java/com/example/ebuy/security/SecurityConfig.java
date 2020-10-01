package com.example.ebuy.security;

import com.example.ebuy.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, UserServiceImpl userServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/users/signup").permitAll()
                .antMatchers("/products/**").permitAll()
                .antMatchers("/users/{username}/**").permitAll()
                .antMatchers("/").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .failureUrl("/users/login-error")
                .permitAll()
                .defaultSuccessUrl("/products/home", true)
                .passwordParameter("password")
                .usernameParameter("username")
                .and()
                .rememberMe()
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/products/home")
                .and()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling().accessDeniedPage("/login");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring().antMatchers("/css/**", "/image/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userServiceImpl);
        return provider;
    }

}
