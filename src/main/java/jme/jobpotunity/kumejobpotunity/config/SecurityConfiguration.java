package jme.jobpotunity.kumejobpotunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF (Cross-Site Request Forgery) ကို ယာယီပိတ်ထားသည်
            // Login form ကို အလုပ်လုပ်ဖို့အတွက် လိုအပ်သည်
            .csrf().disable()
            .authorizeHttpRequests((requests) -> requests
                // public routes: login မလိုဘဲ ဝင်ရောက်ကြည့်ရှုနိုင်သော pages များ
                .requestMatchers("/", "/job/**", "/css/**", "/js/**").permitAll() 
                // Admin role ရှိမှသာ ဝင်ရောက်နိုင်သော pages များ
                .requestMatchers("/newJob", "/editJob/**", "/deleteJob/**").hasRole("ADMIN")
                // ကျန်သော pages များကို authenticated user မှသာ ဝင်ရောက်ခွင့်ပြုသည်
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                // custom login page ကို သုံးမည်
                .loginPage("/login").permitAll()
                // Login အောင်မြင်ပါက home page သို့ redirect ပြန်လုပ်ပေးမည်
                .defaultSuccessUrl("/", true)
            )
            .logout((logout) -> logout
                // Logout လုပ်ဆောင်ချက်ကို လူတိုင်းအတွက် ခွင့်ပြုသည်
                .permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .roles("ADMIN")
            .build();

        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
