package com.pkshop.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ✅ 1. กำหนดการตั้งค่า CORS สำหรับ Spring Security ทั้งระบบ
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://192.168.*:*",
                "http://10.*:*",
                "https://*.vercel.app",
                "*"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // ✅ 2. เปิดใช้งาน CORS ร่วมกับ corsConfigurationSource ด้านบน
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/webhooks/**").permitAll()
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()

                        // 🔓 API ล็อกอิน / สมัครสมาชิก / Refresh Token
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 🔓 เปิดให้บุคคลทั่วไป (Guest) ดูข้อมูลสินค้าและตัวกรองหน้าเว็บได้โดยไม่ต้องมี Token
                        .requestMatchers(HttpMethod.GET, "/api/customer/shop/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/car-brands/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/car-models/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/promotions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer/reviews/**").permitAll()

                        // 🔒 ส่วนของการอัปโหลดไฟล์
                        .requestMatchers("/api/upload/**").hasAnyRole("ADMIN", "CUSTOMER")

                        // 🔒 จำกัดสิทธิ์ตาม Role สำหรับการทำ Transaction (สั่งซื้อ / จัดการระบบ)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/supplier/**").hasRole("SUPPLIER")
                        .requestMatchers("/api/customs/**").hasRole("CUSTOMS")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}