package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.models.KhachHang;
import com.example.banvemaybay.services.KhachHangService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/khach-hang")
public class KhachHangAdminController {

    private final KhachHangService khachHangService;

    public KhachHangAdminController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("")
    public ResponseEntity<?> getListNguoiDatVe(){
        try{
            List<KhachHang> khachHangList = khachHangService.getAllKhachHang();
            return ResponseEntity.ok(khachHangList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
