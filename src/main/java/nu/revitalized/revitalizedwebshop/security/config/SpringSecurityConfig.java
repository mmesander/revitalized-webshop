package nu.revitalized.revitalizedwebshop.security.config;

// Imports
import nu.revitalized.revitalizedwebshop.services.CustomUserDetailService;
import nu.revitalized.revitalizedwebshop.security.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(
            CustomUserDetailService customUserDetailService,
            JwtRequestFilter jwtRequestFilter
    ) {
        this.customUserDetailService = customUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailService);
        return new ProviderManager(auth);
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/*/authorities").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/*/authorities").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/*/authorities/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/users/*/shipping-details").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/discounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/discounts").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.GET, "/users/discounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/discounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/discounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/discounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/discounts/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/users/*/discounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/*/discounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/*/discounts-all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/*/discounts-all").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/auth/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/users/auth/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/users/auth/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/users/auth/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/auth/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET, "/shipping-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/shipping-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/shipping-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/shipping-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/shipping-details/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "products/reviews").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "products/reviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "products/reviews").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "products/reviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "products/reviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "products/reviews/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products/garments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/garments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/products/garments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/garments/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products/supplements").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products/supplements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/supplements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/products/supplements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/supplements/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products/supplements/allergens").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/supplements/allergens/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/products/supplements/allergens/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/supplements/allergens/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/authenticated").authenticated()
                        .requestMatchers("/authenticate").permitAll()
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
