package learn.cosmiclearning.security;


import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@ConditionalOnWebApplication
@Configuration
public class SecurityConfig {


    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration config) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.configure(http));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .requestMatchers(HttpMethod.POST, "/refresh-token").authenticated()
                .requestMatchers(HttpMethod.POST, "/create-account").permitAll()
                .requestMatchers(HttpMethod.GET, "/cosmic-learning/subject", "/cosmic-learning/subject/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/cosmic-learning/subject").hasAuthority("PROFESSOR")
                .requestMatchers(HttpMethod.PUT, "/cosmic-learning/subject/*").hasAuthority("PROFESSOR")
                .requestMatchers(HttpMethod.DELETE, "/cosmic-learning/subject/*").hasAuthority("PROFESSOR")
                .requestMatchers(HttpMethod.GET, "/cosmic-learning/user", "/cosmic-learning/user/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/cosmic-learning/user").authenticated()
                .requestMatchers(HttpMethod.PUT, "/cosmic-learning/user/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/cosmic-learning/review", "/cosmic-learning/review/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/cosmic-learning/review").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.PUT, "/cosmic-learning/review/*").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.DELETE, "/cosmic-learning/review/*").hasAuthority("STUDENT")
                .requestMatchers("/**").denyAll());

        http.addFilter(new JwtRequestFilter(authenticationManager(config), converter));
        http.sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
