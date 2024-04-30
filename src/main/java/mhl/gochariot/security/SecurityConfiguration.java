package mhl.gochariot.security;

import mhl.gochariot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/images/**", "/css/**", "/js/**",
                                "/reviews", "/reviews?**", "/api/reviews/average", "/api/reviews/all",
                                "/api/transits**", "/api/transits","/api/transits/**",
                                "/api/bus**", "/api/bus/**", "/signup", "/api/bus/verified/id/**", "/driver_signup").permitAll()
                        .requestMatchers("/student/**", "/api/reviews/add", "/api/requests/user_requests/**", "/api/requests/add/**").hasAnyAuthority("Student")
                        .requestMatchers("/api/requests/update_status/**").hasAnyAuthority("Student", "Driver")
                        .requestMatchers("/driver/**", "/api/requests/**", "/api/requests/driver_requests/**").hasAnyAuthority("Driver")
                        .requestMatchers("/api/alerts/**").hasAnyAuthority("Driver", "Admin")
                        .requestMatchers("/admin/**", "/api/users/edit/**", "/api/users/verify/**", "/api/users/reject/**").hasAnyAuthority("Admin")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .userDetailsService(userService)
                .logout(logout -> logout
                        .logoutUrl("/signout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }


}
