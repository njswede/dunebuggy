package net.virtualviking.dunebuggy.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import net.virtualviking.dunebuggy.security.VroAuthenticationProvider;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static Log log = LogFactory.getLog(SecurityConfig.class);
	
	@Value("${vro.url}")
	private String vroUrl;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// We require authentication for all pages except some static content.
    	//
        http
            .authorizeRequests()
                .antMatchers("/login", "/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            	.httpBasic()
            .and()
            	.logout().permitAll()
        	.and()
        		.csrf().ignoringAntMatchers("/events/**");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new VroAuthenticationProvider(vroUrl));
        log.info("Authentication initialized for URL " + vroUrl);
        
    }
}