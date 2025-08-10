package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.LoginRequest;
import com.example.banvemaybay.dtos.LoginResponse;
import com.example.banvemaybay.dtos.RegisterRequest;
import com.example.banvemaybay.dtos.VerifyRequest;
import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.Role;
import com.example.banvemaybay.repositorys.NguoiDatRepository;
import com.example.banvemaybay.repositorys.RoleRepostiory;
import com.example.banvemaybay.services.EmailService;
import com.example.banvemaybay.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthencationController {
    @Autowired
    private NguoiDatRepository nguoiDatRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleRepostiory roleRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (!StringUtils.hasText(request.getFullName()) ||
                !StringUtils.hasText(request.getPassword()) ||
                !StringUtils.hasText(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn cần nhập đủ thông tin!");
        }
        Optional<NguoiDat> existingUser = nguoiDatRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            NguoiDat nguoiDat = existingUser.get();

            // Nếu email đã tồn tại nhưng chưa có tài khoản (chưa đăng ký đăng nhập)
            if (nguoiDat.getProvider() == null || nguoiDat.getProvider().isEmpty()) {
                nguoiDat.setProvider("LOCAL");
                nguoiDat.setPassword(passwordEncoder.encode(request.getPassword())); // Lưu mật khẩu
                nguoiDatRepository.save(nguoiDat);
                return ResponseEntity.ok("Tài khoản đã được cập nhật!");
            }

            // Nếu email đã tồn tại với provider = "LOCAL" hoặc "GOOGLE"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã được đăng ký!");
        }

        // Tìm role trong database, nếu không có thì tạo mới
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        //tạo random 6 số
        String activationCode = emailService.generateActivationCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);  // Mã có hiệu lực trong 10 phút
        // Tạo user mới
        NguoiDat nguoiDat = new NguoiDat();
        nguoiDat.setTen(request.getFullName());
        nguoiDat.setEmail(request.getEmail());
        nguoiDat.setPassword(passwordEncoder.encode(request.getPassword()));
        nguoiDat.setEnable(false); // Ban đầu tài khoản chưa kích hoạt
        nguoiDat.setRoles(Set.of(userRole)); // Gán role cho user
        nguoiDat.setProvider("LOCAL");
        nguoiDat.setActivationCode(activationCode);
        nguoiDat.setActivationCodeExpiry(expiryTime);
        nguoiDatRepository.save(nguoiDat);

        emailService.sendActivationEmail(nguoiDat.getEmail(),activationCode);
        return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            // Xác thực tài khoản
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            // Kiểm tra user trong database
            Optional<NguoiDat> optionalNguoiDat = nguoiDatRepository.findByEmail(email);
            if (optionalNguoiDat.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User không tồn tại");
            }

            NguoiDat nguoiDat = optionalNguoiDat.get();

            // Kiểm tra tài khoản đã kích hoạt chưa
            if (!Boolean.TRUE.equals(nguoiDat.getEnable())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User chưa được kích hoạt tài khoản");
            }

            // Lấy danh sách quyền của user
            Set<String> roles = nguoiDat.getRoles().stream()
                    .map(Role::getName) // ROLE_USER, ROLE_ADMIN
                    .collect(Collectors.toSet());

            String token = jwtUtil.generateToken(nguoiDat.getEmail(), roles); // Tạo JWT token từ email và roles
            // Trả về phản hồi đăng nhập thành công
            return ResponseEntity.ok(new LoginResponse(
                    nguoiDat.getTen(),
                    nguoiDat.getEmail(),
                    token
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không đúng!");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyActivationCode(@RequestBody VerifyRequest request) {
        boolean isVerified = emailService.verifyActivationCode(request.getEmail(), request.getCode());
        if (isVerified) {
            return ResponseEntity.ok("Tài khoản của bạn đã được kích hoạt!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã kích hoạt không hợp lệ hoặc đã hết hạn!");
        }
    }
}
