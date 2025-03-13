package payroad.global.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import payroad.global.security.filter.JwtAuthFilter;
import payroad.global.security.filter.LoginFilter;
import payroad.global.security.service.redis.RefreshTokenService;
import payroad.global.security.util.JwtUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // cors 다 해결
            .cors((cors) -> cors
                .configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of(
                            "http://localhost:5173", // 개발 환경
                            "http://localhost:8080", // 개발 환경
                            "http://3.37.61.199:8080",
                            "https://payroad-sooty.vercel.app" // 배포 환경
                        ));

                        configuration.setAllowedMethods(
                            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setAllowCredentials(true);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }))
            //csrf disable
            .csrf(AbstractHttpConfigurer::disable)

            //form login 방식 disable
            .formLogin(AbstractHttpConfigurer::disable)

            //Http basic 인증 방식 disable
            .httpBasic(AbstractHttpConfigurer::disable)

            //경로별 인가 작업
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/mail/**").permitAll()
                .requestMatchers("/members/join").permitAll()
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/token/refresh").permitAll()
                .requestMatchers( //swagger 세팅
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )

            //session 설정
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .addFilterBefore(new JwtAuthFilter(jwtUtil), LoginFilter.class)
            .addFilterAt(
                new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new payroad.global.security.filter.LogoutFilter(jwtUtil, refreshTokenService),
                LogoutFilter.class);
        return http.build();
    }
}