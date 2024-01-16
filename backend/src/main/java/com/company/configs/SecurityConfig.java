package com.company.configs;

import com.company.auth.AuthEntryPointJwt;
import com.company.auth.AuthProvider;
import com.company.filters.AuthFilter;
import com.company.models.enums.Authority;
import com.company.services.user.UserDetailsServiceImpl;
import com.company.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.company.constants.SecurityConstants.PUBLIC_URLS;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((auth) -> auth

//                        .antMatchers("/api/v1/test/**").permitAll()

                        .antMatchers(HttpMethod.POST, "/api/v1/auth/sign-up/admin").hasAuthority(Authority.ADMIN.getAuthority())
                        .antMatchers("/api/v1/auth/**").permitAll()

                        .antMatchers(HttpMethod.GET, "/api/v1/user/{name}").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/user/{id}").authenticated()
                        .antMatchers(HttpMethod.GET, "/api/v1/user").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/v1/user").authenticated()
                        .antMatchers(HttpMethod.PUT, "/api/v1/user").authenticated()
                        .antMatchers(HttpMethod.PUT, "/api/v1/user/{id}").hasAuthority(Authority.ADMIN.getAuthority())

                        .antMatchers("/api/v1/comment/**").authenticated()
                        .antMatchers("/api/v1/post/**").authenticated()
                        .antMatchers("/api/v1/like/**").authenticated()
                )
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthFilter authenticationTokenFilterBean() throws Exception {
        return new AuthFilter(userDetailsService, jwtUtil);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(PUBLIC_URLS);
    }

}
