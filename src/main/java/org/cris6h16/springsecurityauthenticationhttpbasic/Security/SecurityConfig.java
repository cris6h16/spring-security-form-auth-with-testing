package org.cris6h16.springsecurityauthenticationhttpbasic.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // when you specify the loginPage url (not the default config).
        // - you are responsible for rendering the login page, Spring Security won't generate the default

        // I'll use thymeleaf

            /* ----------------------------------------------------------------
             if you cache the request                                       --> default
             * unauthenticated users go: `/auth-info-way1`
             * spring security will redirect to: `/login`
             * after login, spring security will redirect to: `/auth-info-way1` automatically

            // parameter "continue" to redirect after login to the last requested page
            HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
            requestCache.setMatchingRequestParameterName("continue");         --> default
             */
            // ----------------------------------------------------------------*/

            /*
             if you don't cache the request
             * no-authenticated users go: `/auth-info-way1`
             * spring security will redirect to: `/login`
             * after login, spring security will redirect to: `/` automatically (default)

            RequestCache requestCache = new NullRequestCache();
             */

        return http
                .authorizeHttpRequests(req->req  // necessary when .formLogin() is used
                        .anyRequest().permitAll())
                .formLogin(config -> config
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/login")
                        // use the defaults if you aren't comfortable configuring this

//                        .failureForwardUrl("/login?error")    // handled in thyemleaf
//                        .successForwardUrl("/")               // if you don't want to go to the cache URL
//                        .defaultSuccessUrl("/", false)        // ( path, alwaysUse )
                        .permitAll())
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout")
//                        .permitAll())/
//                .requestCache(cache -> cache.requestCache(requestCache)) // is the default
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .build();
        /*
        - The Form:
          1. POST to /login
            - pass the:
                - username in an input named "username"
                - password in an input named "password"
                - include a CSRF token (thymeleaf include it automatically)

                --// Optional \\--
          3. If the HTTP parameter named `error` is found
                - it indicates the user failed to provide a valid username or password.
          4. If the HTTP parameter named `logout` is found
                - it indicates the user has logged out successfully.

                    <div th:if="${param.error}">
                        Invalid username and password.</div>
                    <div th:if="${param.logout}">
                        You have been logged out.</div>

         */
    }

    /* InMemoryUserDetailsManager is a simple in-memory implementation of UserDetailsService
     and creates users easily.. for testing purposes

     you implemented it, then Spring Security will use it to authenticate users, also
     Spring Security won't generate a default login credential ( user, password-in-logs )
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder() // you can do your own impl for add more attributes
                .username("cris6h16")
                .password("cris6h16") // password is stored encrypted
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);// works like a password storage
    }

    // implementations of UserDetailsService Example non-in-memory
    /*
    @Service
    public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0])) // Assuming roles are stored as a collection in User entity
                .build();
    }
}


     */
}
