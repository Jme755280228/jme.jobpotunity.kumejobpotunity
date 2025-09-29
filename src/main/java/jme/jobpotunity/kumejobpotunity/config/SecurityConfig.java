// src/main/java/.../config/SecurityConfig.java

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
// 💡 FIX: AntPathRequestMatcher အစား PathRequestMatcher ကို အသုံးပြုပါမည်။
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; 
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector; // MvcRequestMatcher အတွက် လိုအပ်သည်။

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // HandlerMappingIntrospector ကို Inject လုပ်ရပါမယ်။ (MvcRequestMatcher အတွက်)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        
        // MvcRequestMatcher ကို အသုံးပြုရန်
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
            // 1. CSRF protection ကို H2 console အတွက် ignore လုပ်ထားတယ်
            .csrf(csrf -> csrf
                // 💡 FIX: AntPathRequestMatcher (Deprecated) အစား mvcMatcherBuilder ကို သုံးပါမည်
                .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")) 
                // Spring Boot 3.2+ တွင် PathRequestMatcher ကို သုံးခွင့်ပြုသည်
            )
            .authorizeHttpRequests(authorize -> authorize
                // 💡 FIX: AntPathRequestMatcher (Deprecated) အစား mvcMatcherBuilder ကို သုံးပါမည်
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll() // H2 Console ကို ဝင်ခွင့်ပေးရန်
                .requestMatchers(mvcMatcherBuilder.pattern("/"), mvcMatcherBuilder.pattern("/register"), 
                                 mvcMatcherBuilder.pattern("/css/**"), mvcMatcherBuilder.pattern("/js/**"), 
                                 mvcMatcherBuilder.pattern("/job/{id}"), mvcMatcherBuilder.pattern("/uploads/**")).permitAll()

                // Role based access
                .requestMatchers(mvcMatcherBuilder.pattern("/employer/**")).hasRole("EMPLOYER")
                .requestMatchers(mvcMatcherBuilder.pattern("/newJob"), mvcMatcherBuilder.pattern("/saveJob"), 
                                 mvcMatcherBuilder.pattern("/editJob/{id}"), mvcMatcherBuilder.pattern("/updateJob/{id}"), 
                                 mvcMatcherBuilder.pattern("/deleteJob/{id}"), mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                .requestMatchers(mvcMatcherBuilder.pattern("/job/apply/{id}")).hasRole("APPLICANT")

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

        // H2 Console အတွက်
        http.headers(headers -> headers
             .frameOptions(frameOptions -> frameOptions.sameOrigin()) 
        );
        
        return http.build();
    }
    
    // AntPathRequestMatcher က deprecate ဖြစ်တဲ့အတွက် MvcRequestMatcher သုံးရင် HandlerMappingIntrospector bean လိုအပ်ပါသည်။
    // AntPathRequestMatcher.antMatcher("/h2-console/**") ကိုတော့ ၎င်းဟာ Static Resource ဖြစ်တဲ့အတွက် အသုံးပြုခွင့်ပေးထားပါတယ်။

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

