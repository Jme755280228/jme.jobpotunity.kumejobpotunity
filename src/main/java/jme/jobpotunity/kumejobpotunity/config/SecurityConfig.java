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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // HandlerMappingIntrospector ကို Inject လုပ်ရပါမယ်။ (MvcRequestMatcher အတွက်)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        // MvcRequestMatcher ကို အသုံးပြုရန် Builder တည်ဆောက်သည်
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
            // 1. CSRF protection ကို H2 console အတွက် ignore လုပ်ထားသည်
            .csrf(csrf -> csrf
                // H2 console အတွက် လိုအပ်သည် (SQLite သုံးနေသော်လည်း နောင်လေ့လာရန် ထားရှိသည်)
                .ignoringRequestMatchers(mvcMatcherBuilder.pattern("/h2-console/**"))
            )
            .authorizeHttpRequests(authorize -> authorize

                // 💡 Public Access 💡
                .requestMatchers(mvcMatcherBuilder.pattern("/h2-console/**")).permitAll() 
                .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()

                // Static Resources
                .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/images/**")).permitAll() 
                .requestMatchers(mvcMatcherBuilder.pattern("/uploads/**")).permitAll()

                // Job Listings (လူတိုင်းကြည့်ခွင့်ရှိရန်)
                .requestMatchers(mvcMatcherBuilder.pattern("/jobs")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/jobs/{id}")).permitAll()

                // 💡 Role based access 💡
                
                // ADMIN ၏ လုပ်ငန်းများ (Job Approval နှင့် Admin Panel အားလုံး)
                // /admin/jobs/** တွင် approve လမ်းကြောင်းလည်း ပါဝင်သည်
                .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                
                // EMPLOYER ၏ Dashboard နှင့် လုပ်ငန်းများ (Job Post, Edit, Delete, View Applicants)
                .requestMatchers(mvcMatcherBuilder.pattern("/employer/**")).hasRole("EMPLOYER")

                // APPLICANT အတွက်
                .requestMatchers(mvcMatcherBuilder.pattern("/applicant/apply/{id}")).hasRole("APPLICANT")
                .requestMatchers(mvcMatcherBuilder.pattern("/profile/**")).hasAnyRole("APPLICANT", "EMPLOYER") // Profile view/edit

                // ကျန်တဲ့ requests အားလုံးကို authenticate လုပ်ဖို့ လိုအပ်သည်
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

        // H2 Console အတွက် (Frame ဖြင့် ဝင်ရောက်ကြည့်ရှုနိုင်ရန်)
        http.headers(headers -> headers
             .frameOptions(frameOptions -> frameOptions.sameOrigin())
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

        // Login ပြီးဆုံးပါက Role အလိုက် Redirect လုပ်ရန်
        handler.setRedirectStrategy((request, response, url) -> {
            if (request.isUserInRole("ROLE_ADMIN")) {
                response.sendRedirect("/admin/jobs");
            }
            else if (request.isUserInRole("ROLE_EMPLOYER")) {
                response.sendRedirect("/employer/jobs");
            }
            else {
                response.sendRedirect("/");
            }
        });

        return handler;
    }
}


