package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.UserRoleAdminDTO;
import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.services.NguoiDatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/admin/nguoi_dat")
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



}
