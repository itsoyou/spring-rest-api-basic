package com.example.rest.webservices.restful_web_services.security;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration
public class SpringSecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/**
		 * This overrides the default filterChain of Spring Security.
		 * */
		
		// 1. All requests should be authenticated
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
		
		// 2. if a request is not authenticated, web page is shown for log in
		// in this case, we switch to the pop up, instead of login page.
		http.httpBasic(withDefaults());
		
		// 3. CSRF-> POST, PUT
		http.csrf((csrf) -> csrf.disable());

		return http.build();
	}
}
