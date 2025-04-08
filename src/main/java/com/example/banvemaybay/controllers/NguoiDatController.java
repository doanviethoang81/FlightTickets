package com.example.banvemaybay.controllers;

import com.example.banvemaybay.components.ActivationCodeCache;
import com.example.banvemaybay.dtos.ChangePasswordDTO;
import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.VerifyRequest;
import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.ThongTinDatVe;
import com.example.banvemaybay.repositorys.NguoiDatRepository;
import com.example.banvemaybay.services.*;
import com.example.banvemaybay.utils.NumberEncryptor;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix:/api/v1}/nguoi_dat")
@RequiredArgsConstructor
public class NguoiDatController {

    private final NguoiDatService nguoiDatService;
    private final ThongTinDatVeService thongTinDatVeService;
    private final PhieuDatVeService phieuDatVeService;
    private final KhachHangService khachHangService;
    private final ChuyenBayService chuyenBayService;
    private final EmailService emailService;
    private final NguoiDatRepository nguoiDatRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ActivationCodeCache activationCodeCache;

//    private final ActivationCodeCache activationCodeCache;


    //tìm người đặt sdt
    @GetMapping("/{sdt}")
    public ResponseEntity<?> timVeTheoSdtNguoiDat(
            @PathVariable("sdt") String sdt
    ){
        try{
//            List<PhieuDatVeDTO> phieuDatVeDTOList = phieuDatVeService.getThongTinVeTheoSdt(sdt);
            NguoiDatDTO nguoiDatDTO = nguoiDatService.timVeDaDatTheoSDT(sdt);
//            List<ThongTinDatVeDTO> listThongTinDatVeDTO = phieuDatVeService.getThongTinVeTheoSdt(sdt);
//            NguoiDat nguoiDat = nguoiDatService.timVeDaDatTheoSDT(sdt);
//            ThongTinDatVe thongTinDatVe= thongTinDatVeService.getThongTinDatVe(nguoiDat);
//            List<PhieuDatVe> phieuDatVeList = phieuDatVeService.getPhieuDatVe(thongTinDatVe.getId());
            return ResponseEntity.ok(nguoiDatDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //đặt vé
    @PostMapping("/dat-ve")
    public ResponseEntity<?> datVe(@RequestBody JsonNode json) {
        try {
            ThongTinDatVe thongTinDatVe= nguoiDatService.xuLyDatVe(json);
            if (thongTinDatVe != null) {
                double totalMoney = thongTinDatVe.getTongTien().doubleValue();
                int orderId = thongTinDatVe.getId();

                String encryptedId = NumberEncryptor.encryptId(orderId);
                String redirectUrl = "http://localhost:8080/api/v1/payment/vn-pay?amount=" + (long)(totalMoney * 100) + "&id=" + encryptedId;

                // Tạo trang HTML có script chuyển hướng
                String htmlContent = "<html><head>"
                        + "<meta http-equiv='refresh' content='0;url=" + redirectUrl + "'/>"
                        + "<script>window.location.href = '" + redirectUrl + "';</script>"
                        + "</head><body>"
                        + "Nếu bạn không được chuyển hướng, <a href='" + redirectUrl + "'>bấm vào đây</a>."
                        + "</body></html>";

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "text/html")
                        .body(htmlContent);
            } else {
                return ResponseEntity.ok("Đặt vé thất bại");
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi xử lý dữ liệu: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> getKiemTraLoaiTaiKhoan(@PathVariable String email) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean coMatKhau = nguoiDatService.kiemTraLoaiTaiKhoan(email);
            if (coMatKhau) {
                response.put("url", "http://localhost:5173/user/change-password-user/" + email);
                response.put("message", "Đã có mật khẩu, chuyển tới trang đổi mật khẩu.");
            } else {
                response.put("url", "http://localhost:5173/user/new-password-user/" + email);
                response.put("message", "Bạn đăng nhập bằng Google nên cần tạo mật khẩu mới.");
                //tạo random 6 số
                String activationCode = emailService.generateActivationCode();
                LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);  // Mã có hiệu lực trong 10 phút

                activationCodeCache.saveCode(email, activationCode, expiryTime);

                emailService.sendPasswordUpdateEmail(email,activationCode);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getThongTinNguoiDatTheoEmail(@PathVariable("email") String email) {
        try {
            NguoiDat nguoiDat = nguoiDatService.timThongTinNguoiDatTheoEmail(email);
            Map<String, Object> response = new HashMap<>();
            response.put("ho", nguoiDat.getHo());
            response.put("ten", nguoiDat.getTen());
            response.put("soDienThoai", nguoiDat.getSoDienThoai());
            response.put("email", nguoiDat.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/change-password-user/{email}")
    public ResponseEntity<String> changePassword(
            @PathVariable("email") String email,
            @RequestBody ChangePasswordDTO changePassword
    ) {
        try {
            // Kiểm tra và thay đổi mật khẩu
            boolean isPasswordChanged = nguoiDatService.changePassword(email, changePassword);
            if (isPasswordChanged) {
                return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công.");
            } else if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu mới không giống nhau!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu cũ không chính xác!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }


    @PutMapping("/create-password/{email}")
    public ResponseEntity<?> taoMatKhauUser(@PathVariable("email") String email, @RequestBody Map<String, String> request) {
        try {
            String password = request.get("password");
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Mật khẩu không được để trống!");
            }

            NguoiDat nguoiDat = nguoiDatService.timThongTinNguoiDatTheoEmail(email);
            if (nguoiDat == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy người dùng với email: " + email);
            }

            nguoiDat.setPassword(passwordEncoder.encode(password));
            nguoiDatRepository.save(nguoiDat);

            return ResponseEntity.ok("Xác nhận gmail!");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo mật khẩu: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyActivationCode(@RequestBody VerifyRequest request) {
        Map<String, Object> response = new HashMap<>();

        boolean isVerified = emailService.verifyActivationCodeUser(request.getEmail(), request.getCode());

        if (isVerified) {

            response.put("success", true);
            response.put("message", "Tạo mật khẩu thành công!");
            return ResponseEntity.ok(response);
        } else {
            NguoiDat user = nguoiDatRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            user.setPassword("");
            nguoiDatRepository.save(user);
            response.put("success", false);
            response.put("message", "Mã xác minh không đúng hoặc đã hết hạn!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
