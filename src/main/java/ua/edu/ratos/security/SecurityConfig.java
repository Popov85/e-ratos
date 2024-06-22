package ua.edu.ratos.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Global security config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticatedUserDetailsService authenticatedUserDetailsService;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
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
                    fl.successHandler((request, response, authentication) -> {
                        response.setContentType("application/json");
                        response.getWriter().write("{\"message\":\"Success\"}");
                    });
                    fl.failureHandler((request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"" + exception.getMessage() + "\"}");
                    });
                })
                .rememberMe(rme -> rme.key("e-ratos"))
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                            .logoutSuccessHandler((request, response, authentication) -> {
                                response.setContentType("application/json");
                                response.getWriter().write("{\"message\":\"Logout successful\"}");
                            })
                            .deleteCookies("JSESSIONID")
                            .invalidateHttpSession(true);
                })
                .userDetailsService(authenticatedUserDetailsService)
                .headers(headers -> headers.
                        frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) ->
                                response.setStatus(HttpStatus.UNAUTHORIZED.value())) // Handle not authenticated
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.setStatus(HttpStatus.FORBIDDEN.value())) // Handle not authorized
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
