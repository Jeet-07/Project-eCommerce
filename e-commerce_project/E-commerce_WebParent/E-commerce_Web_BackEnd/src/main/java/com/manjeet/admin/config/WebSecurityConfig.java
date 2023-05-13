package com.manjeet.admin.config;

import com.manjeet.admin.config.customDetailsSerive.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.formLogin(withDefaults()).httpBasic(withDefaults());
//        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/images/**","/js/**","/webjars/**").permitAll()
                .requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
                .requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
                .requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
                .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                .requestMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
                .requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")
                .requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
                .requestMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")
                .anyRequest().authenticated();

        http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/", true);
        http.logout().permitAll();
        http.rememberMe().key("dksjbfkshvks2534gfbgdb$3#bvkbvbvj");


        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
