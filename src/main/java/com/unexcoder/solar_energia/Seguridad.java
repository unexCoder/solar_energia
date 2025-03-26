package com.unexcoder.solar_energia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// configuracion de seguridad
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Seguridad {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN") //acceso full admins
                .requestMatchers("/css/", "/js/", "/img/", "/**").permitAll() //acceso full
                .requestMatchers("/login","/registrar").permitAll() //acceso full admins
                .anyRequest().authenticated() // requiere autenticar
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .defaultSuccessUrl("/inicio", true)
                .usernameParameter("email")
                .passwordParameter("password")
                // .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout((logout) -> logout 
                        .logoutUrl("/logout") 
                        .logoutSuccessUrl("/") 
                        .permitAll())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
