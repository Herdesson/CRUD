package org.example.SpringBootEssencies.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.SpringBootEssencies.Service.ClientUserDetailsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@SuppressWarnings("java.S5344")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ClientUserDetailsService clientUserDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/animes/all").hasRole("ADMIN")
                .antMatchers("/animes/**").hasRole("USER")
                .anyRequest().authenticated().
                and().formLogin().and().httpBasic();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder password = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("password encoded{}", password.encode("test"));
        auth.inMemoryAuthentication()
                .withUser("flavio")
                .password(password.encode("herdesson"))
                .roles("USER","ADMIN")
                .and()
                .withUser("herdesson")
                .password(password.encode("flavio"))
                .roles("USER");
        auth.userDetailsService(clientUserDetailsService).passwordEncoder(password);
    }
}
