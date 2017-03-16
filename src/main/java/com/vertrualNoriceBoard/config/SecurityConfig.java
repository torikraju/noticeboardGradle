package com.vertrualNoriceBoard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.formLogin()
			.loginPage("/login").defaultSuccessUrl("/")
			.and()
			.logout().logoutSuccessUrl("/")
			.and()
			.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/profile","/profile/**","/changePassword","/changePhoto").authenticated()
			.antMatchers("/postNotice","/updateNotice/**","/deleteNotice/**").authenticated()
			.antMatchers("/yourNotice").authenticated()
			.antMatchers("/individualNotice/**").permitAll()
			.antMatchers("/contactUs").permitAll()
			.antMatchers("/allNotice").permitAll()
			.antMatchers("/getProfilePic/**").permitAll()
			.antMatchers("/individualNotice").permitAll()
			.antMatchers("//updateProfile").authenticated()
			.antMatchers("/userManage","/AdminAction/**","/noticeManage").access("hasAnyAuthority('ROLE_ADMIN','ROLE_SYSTEMADMIN')")
			.antMatchers("/mail","/individualMail/**").access("hasAnyAuthority('ROLE_ADMIN','ROLE_SYSTEMADMIN')")
			.antMatchers("/adminManage","/SystemAdminAction/**","/createAdmin").access("hasAuthority('ROLE_SYSTEMADMIN')")
			.antMatchers("/bootstrap/**","/pic/**","/webjars/**").permitAll()
			.antMatchers("/chat", "/mywebsocket/**", "/topic/**","/app/**").access("isAuthenticated()")
			.antMatchers("/login","/registration").access("!isAuthenticated()")
			.anyRequest().denyAll()
			.and().rememberMe();
		
	}
	
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		SpringSecurityDialect dialect = new SpringSecurityDialect();
		return dialect;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

}
