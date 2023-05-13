package com.manjeet.security;

import com.manjeet.security.oauth.CustomerOAuth2UserService;
import com.manjeet.security.oauth.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig{

	private final CustomerOAuth2UserService oAuth2UserService;
	private final OAuth2LoginSuccessHandler oauth2LoginHandler;
	private final DatabaseLoginSuccessHandler databaseLoginHandler;
    private final CustomerUserDetailsService customerUserDetailsService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
		http.authorizeHttpRequests()
				.requestMatchers("/images/**","/js/**","/webjars/**").permitAll()
				.requestMatchers("/account_details", "/update_account_details", "/orders/**",
						"/cart", "/address_book/**", "/checkout", "/place_order", "/reviews/**",
						"/process_paypal_order", "/write_review/**", "/post_review").authenticated()
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(databaseLoginHandler)
				.permitAll()
				.and()
				.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(oAuth2UserService)
				.and()
				.successHandler(oauth2LoginHandler)
				.and()
				.logout().permitAll()
				.and()
				.rememberMe()
				.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
				.tokenValiditySeconds(14 * 24 * 60 * 60)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		;
		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customerUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
