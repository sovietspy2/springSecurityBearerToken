package wortex.stream.securityexample.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wortex.stream.securityexample.demo.repository.UserRepository;
import wortex.stream.securityexample.demo.security.JwtTokenFilter;
import wortex.stream.securityexample.demo.security.JwtTokenProvider;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain springWebFilterChain(HttpSecurity http,
                                             JwtTokenProvider tokenProvider) throws Exception {
            return http
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(c-> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                    .authorizeRequests(c -> c
                            .antMatchers("/signIn").permitAll()
                            .antMatchers(HttpMethod.GET, "/public").permitAll()
                            .antMatchers(HttpMethod.GET, "/person/find/**").hasRole("ADMIN")
                            .antMatchers(HttpMethod.GET, "/person/delete/**").hasRole("SUPER_ADMIN")
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                    .build();

    }

    @Bean
    @Qualifier(value="custom")
    UserDetailsService customUserDetailsService(UserRepository users) {
        return (username) -> users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }

    @Bean
    AuthenticationManager customAuthenticationManager(@Qualifier("custom") UserDetailsService userDetailsService, PasswordEncoder encoder) {
        return authentication -> {
            String username = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";

            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (!encoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Bad credentials");
            }

            if (!user.isEnabled()) {
                throw new DisabledException("User account is not active");
            }

            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        };
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
