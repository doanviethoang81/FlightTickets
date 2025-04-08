package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.dtos.ChangePasswordDTO;
import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.UserRoleAdminDTO;
import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.services.NguoiDatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/nguoi_dat")
@RequiredArgsConstructor
public class NguoiDatAdminController {

    private final NguoiDatService nguoiDatService;

    @GetMapping("")
    public ResponseEntity<?> getListNguoiDatVe(){
        List<NguoiDat> nguoiDats = nguoiDatService.getAllNguoiDat();
        return ResponseEntity.ok(nguoiDats);
    }

    @GetMapping("/{sdt}")
    public ResponseEntity<?> getThongTinDatVe(@PathVariable("sdt") String sdt){
        try{
            NguoiDatDTO nguoiDatDTO = nguoiDatService.timVeDaDatTheoSDT(sdt);
            return ResponseEntity.ok(nguoiDatDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<?> getUsersByRoleName(@PathVariable String roleName) {
        List<UserRoleAdminDTO> users = nguoiDatService.getAllNguoiDatByRoleName(roleName);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về mã 204 nếu không có người dùng
        }
        return ResponseEntity.ok(users); // Trả về mã 200 nếu có người dùng
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
}
