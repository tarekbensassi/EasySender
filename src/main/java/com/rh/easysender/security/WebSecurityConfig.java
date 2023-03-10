package com.rh.easysender.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable().authorizeRequests()
		
	  	.antMatchers("/**/**/*.js","/**/**/*.js","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpg", "/**/*.css").permitAll()
     	.antMatchers("/**/dist/**","/**/plugins/**","/**/easysender/**").permitAll()
		
	     	.antMatchers("/index/**","/sendmail/**","/profile/**").hasAnyAuthority("ADMIN")
	     //	.antMatchers("/").permitAll()
	     
		
	     	.anyRequest().authenticated()
			.and()
			.formLogin()
			      .loginPage("/login")
			      .usernameParameter("username")
			      .defaultSuccessUrl("/index", true)
			      .permitAll()
			
			.and()
			.logout()
			.permitAll()
            .deleteCookies("JSESSIONID")
            .and()
            .rememberMe().rememberMeParameter("remember-me")
			
			
		//	.and()
		//	.exceptionHandling().accessDeniedPage("/403")
			;
	}
	

	@Override
	public void configure(WebSecurity web) throws Exception {  
	    web
	            .ignoring()
	            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**")

	            ;
	    
	}
}
