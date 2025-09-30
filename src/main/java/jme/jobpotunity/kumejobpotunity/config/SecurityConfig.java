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
                
                // 💡 NEW: Static Resources (CSS, JS, Images, Uploads) နှင့် Public Pages အားလုံးကို ဝင်ခွင့်ပေးရန်
                .requestMatchers(mvcMatcherBuilder.pattern("/h2-console/**")).permitAll() // H2 Console ကို ဝင်ခွင့်ပေးရန်
                .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
                
                // 💡 NEW: အလုပ်စာရင်း နှင့် အလုပ်အသေးစိတ်ကို လူတိုင်းကြည့်ခွင့်ရှိရန်
                .requestMatchers(mvcMatcherBuilder.pattern("/jobs")).permitAll() 
                .requestMatchers(mvcMatcherBuilder.pattern("/jobs/{id}")).permitAll() // မူရင်းတွင် /job/{id} ဖြစ်သည်၊ /jobs/{id} သို့ ပြောင်းလိုက်သည်
                
                // Static Resource Paths များ
                .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll() 
                .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll() 
                .requestMatchers(mvcMatcherBuilder.pattern("/images/**")).permitAll() // 💡 NEW: Header အတွက် Image Access ခွင့်ပြုသည်
                .requestMatchers(mvcMatcherBuilder.pattern("/uploads/**")).permitAll()

                // Role based access
                // EMPLOYER ၏ Dashboard နှင့် လုပ်ငန်းများ
                .requestMatchers(mvcMatcherBuilder.pattern("/employer/**")).hasRole("EMPLOYER")
                
                // ADMIN ၏ လုပ်ငန်းများ (Job Management အားလုံးကို Admin ဖြင့် ကာကွယ်ထားသည်)
                .requestMatchers(
                    mvcMatcherBuilder.pattern("/newJob"), 
                    mvcMatcherBuilder.pattern("/saveJob"), 
                    mvcMatcherBuilder.pattern("/editJob/{id}"), 
                    mvcMatcherBuilder.pattern("/updateJob/{id}"), 
                    mvcMatcherBuilder.pattern("/deleteJob/{id}"), 
                    mvcMatcherBuilder.pattern("/admin/**")
                ).hasRole("ADMIN")
                
                // APPLICANT အတွက်
                .requestMatchers(mvcMatcherBuilder.pattern("/job/apply/{id}")).hasRole("APPLICANT")

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

