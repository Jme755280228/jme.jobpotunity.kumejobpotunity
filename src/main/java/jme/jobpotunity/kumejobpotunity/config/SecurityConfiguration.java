// src/main/java/jme/jobpotunity/kumejobpotunity/config/SecurityConfiguration.java

package jme.jobpotunity.kumejobpotunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // import BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // import PasswordEncoder
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // PasswordEncoder ကို bean အဖြစ် ထည့်သွင်းပေးသည်
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((requests) -> requests
                // /register ကို public access ပေးထားသည်
                .requestMatchers("/", "/job/**", "/css/**", "/js/**", "/register").permitAll() 
                .requestMatchers("/newJob", "/editJob/**", "/deleteJob/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
