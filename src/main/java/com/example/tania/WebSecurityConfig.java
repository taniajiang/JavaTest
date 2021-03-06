package com.example.tania;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity 
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	/**
	 * Create two user in memory to auth.
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
				.withUser("testadmin")
				.password(new BCryptPasswordEncoder().encode("adminpassword")).roles("ADMIN")
		.and().withUser("testUser")
				.password(new BCryptPasswordEncoder().encode("userpassword")).roles("USER");
	}

	/**
	 * Config login check request and success forward request.
	 *
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.formLogin()
				.loginProcessingUrl("/login")
				.successForwardUrl("/search")
				.and()
				.authorizeRequests()
				.antMatchers("/login.html", "/login",
						"/logout", "/").permitAll()
				.anyRequest()
				.authenticated();
	}

}
