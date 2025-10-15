package jme.jobpotunity.kumejobpotunity.config;

import jme.jobpotunity.kumejobpotunity.domain.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);

        http
            .csrf(csrf -> csrf.ignoringRequestMatchers(mvc.pattern("/h2-console/**")))
            .authorizeHttpRequests(auth -> auth
                // --- Public Access ---
                .requestMatchers(
                    mvc.pattern("/"), mvc.pattern("/register"), mvc.pattern("/login"),
                    mvc.pattern("/css/**"), mvc.pattern("/js/**"), mvc.pattern("/images/**"),
                    mvc.pattern("/jobs"), mvc.pattern("/jobs/{id}")
                ).permitAll()

                // --- Role-Based Access ---
                // ADMIN can access /admin/** URLs
                .requestMatchers(mvc.pattern("/admin/**"))
                    .hasAuthority(UserRole.ROLE_ADMIN.name())

                // EMPLOYER can access /employer/** URLs
                .requestMatchers(mvc.pattern("/employer/**"))
                    .hasAuthority(UserRole.ROLE_EMPLOYER.name())

                // APPLICANT can access /applicant/** and their profile editing pages
                .requestMatchers(mvc.pattern("/applicant/**"), mvc.pattern("/profile/edit"), mvc.pattern("/profile/save"))
                    .hasAuthority(UserRole.ROLE_APPLICANT.name())
                
                // Any other request needs to be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}


