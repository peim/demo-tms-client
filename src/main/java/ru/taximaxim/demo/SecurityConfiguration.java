package ru.taximaxim.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ru.taximaxim.demo.security.CustomAuthenticationProvider;
import ru.taximaxim.demo.security.CustomLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;
    
    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler).logoutUrl("/logout")
                    .logoutSuccessUrl("/home").invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID").permitAll();

        http.sessionManagement().maximumSessions(1);
        
        /* http.requiresChannel().anyRequest().requiresSecure();
        
        http
            .portMapper()
                .http(8080).mapsTo(8443);
        */
    }
}
