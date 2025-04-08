package com.example.banvemaybay.configs;

import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.Role;
import com.example.banvemaybay.repositorys.NguoiDatRepository;
import com.example.banvemaybay.repositorys.RoleRepostiory; // Sửa typo
import com.example.banvemaybay.services.CustomUserDetailService;
import com.example.banvemaybay.utils.JWTUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.URLEncoder;
import java.util.Optional;
import java.util.Set;

@Getter
@Configuration
@EnableWebSecurity
public class SecurityConfigs {

    private final CustomUserDetailService customUserDetailService;
    private final JWTUtil jwtUtil;

    @Autowired
    private NguoiDatRepository nguoiDatRepository;

    @Autowired
    private RoleRepostiory roleRepository; // Sửa typo từ RoleRepostiory

    public SecurityConfigs(CustomUserDetailService customUserDetailService, JWTUtil jwtUtil) {
        this.customUserDetailService = customUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").permitAll()
                        .requestMatchers("/api/v1/posts/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(Customizer.withDefaults())
                .addFilterBefore(new JWTAuthenticationFilter(jwtUtil, customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("http://localhost:5173", true) // Cập nhật cho Render
                        .successHandler(authenticationSuccessHandler())
                );
        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User user = token.getPrincipal();
            String email = user.getAttribute("email");
            String firstName = user.getAttribute("name");
            String avatar = user.getAttribute("picture");

            Optional<NguoiDat> existingUser = nguoiDatRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                NguoiDat nguoiDat = existingUser.get();
                if ("LOCAL".equals(nguoiDat.getProvider())) {
                    String redirectUrlError = "http://localhost:5173?error=" +
                            URLEncoder.encode("Tài khoản này đã đăng ký bằng email vui lòng đăng nhập bằng mật khẩu!", "UTF-8");
                    response.setHeader("Content-Type", "text/html; charset=UTF-8");
                    response.sendRedirect(redirectUrlError);
                    return;
                }
                nguoiDat.setTen(firstName);
                nguoiDatRepository.save(nguoiDat);
            } else {
                NguoiDat nguoiDat = new NguoiDat();
                nguoiDat.setEmail(email);
                nguoiDat.setTen(firstName);
                nguoiDat.setEnable(true);
                nguoiDat.setProvider("GOOGLE");
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName("ROLE_USER");
                            return roleRepository.save(newRole);
                        });
                nguoiDat.setRoles(Set.of(userRole));
                nguoiDatRepository.save(nguoiDat);
            }
            String tokenJWT = jwtUtil.generateToken(email, Set.of("ROLE_USER"));
            String redirectUrl = "http://localhost:5173?name=" + URLEncoder.encode(firstName, "UTF-8")
                    + "&avatar=" + avatar
                    + "&email=" + email
                    + "&token=" + tokenJWT;
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            response.sendRedirect(redirectUrl);
        };
    }
}