package ua.edu.ratos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import ua.edu.ratos.security.lti.LTIAwareAccessDeniedHandler;
import ua.edu.ratos.security.lti.LTIAwareAuthenticationFailureHandler;
import ua.edu.ratos.security.lti.LTIAwareAuthenticationSuccessHandler;

/**
 * Global security config, LTI aware
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticatedUserDetailsService authenticatedUserDetailsService;

    private final LTIAwareAuthenticationSuccessHandler ltiAwareAccessSuccessHandler;

    private final LTIAwareAuthenticationFailureHandler ltiAwareAuthenticationFailureHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/login*").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/sign-up*", "/access-denied*").permitAll()
                        .requestMatchers("/actuator/**", "/self-registration/**").permitAll()
                        .requestMatchers("/*.js", "/*.css", "/*.png").permitAll()
                        .requestMatchers("/user/**", "/student/**", "/session/**", "/info/**").hasAnyRole("LMS_USER", "STUDENT", "LAB_ASSISTANT", "INSTRUCTOR", "DEP_ADMIN", "FAC_ADMIN", "ORG_ADMIN", "GLOBAL_ADMIN")
                        .requestMatchers("/department/**").hasAnyRole("LAB_ASSISTANT", "INSTRUCTOR", "DEP_ADMIN", "FAC_ADMIN", "ORG_ADMIN", "GLOBAL_ADMIN")
                        .requestMatchers("/instructor/**").hasAnyRole("INSTRUCTOR", "DEP_ADMIN", "FAC_ADMIN", "ORG_ADMIN", "GLOBAL_ADMIN")
                        .requestMatchers("/dep-admin/**").hasAnyRole("DEP_ADMIN", "FAC_ADMIN", "ORG_ADMIN", "GLOBAL_ADMIN")
                        .requestMatchers("/fac-admin/**").hasAnyRole("FAC_ADMIN", "ORG_ADMIN", "GLOBAL_ADMIN")
                        .requestMatchers("/org-admin/**").hasAnyRole("ORG_ADMIN", "GLOBAL_ADMIN")
                        .requestMatchers("/global-admin/**").hasRole("GLOBAL_ADMIN")
                        .requestMatchers("/lti/**").hasAnyRole("LTI")
                        .requestMatchers("/lms/**").hasRole("LMS_USER")
                        .anyRequest().authenticated()
                )
                .formLogin(fl -> {
                    fl.loginPage("/login");
                    fl.successHandler(ltiAwareAccessSuccessHandler);
                    fl.failureHandler(ltiAwareAuthenticationFailureHandler);
                })
                .rememberMe(rme -> rme.key("e-ratos"))
                .userDetailsService(authenticatedUserDetailsService)
                .headers(headers -> headers.
                        frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(x -> x.accessDeniedHandler(ltiAwareAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler ltiAwareAccessDeniedHandler() {
        return new LTIAwareAccessDeniedHandler();
    }
}
