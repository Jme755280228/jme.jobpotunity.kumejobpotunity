package jme.jobpotunity.kumejobpotunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF protection ကို h2-console အတွက် ignore လုပ်ထားတယ်
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .authorizeHttpRequests(authorize -> authorize
                // Public ဝင်ခွင့်ရှိတဲ့ pages တွေ
                .requestMatchers("/", "/register", "/css/**", "/job/{id}", "/uploads/**").permitAll()
                // Admin role ရှိမှ ဝင်ခွင့်ရှိတဲ့ pages တွေ
                .requestMatchers("/newJob", "/saveJob", "/editJob/{id}", "/updateJob/{id}", "/deleteJob/{id}", "/admin/**").hasRole("ADMIN")
                // User role ရှိမှ ဝင်ခွင့်ရှိတဲ့ pages တွေ
                .requestMatchers("/job/apply/{id}").hasRole("USER")
                // ကျန်တဲ့ requests အားလုံးကို authenticate လုပ်ဖို့ လိုအပ်တယ်
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/");
        handler.setTargetUrlParameter("redirectTo");
        handler.setAlwaysUseDefaultTargetUrl(false);

        handler.setRedirectStrategy((request, response, url) -> {
            if (request.isUserInRole("ROLE_ADMIN")) {
                response.sendRedirect("/admin/jobs");
            } else {
                response.sendRedirect("/");
            }
        });
        
        return handler;
    }
}
