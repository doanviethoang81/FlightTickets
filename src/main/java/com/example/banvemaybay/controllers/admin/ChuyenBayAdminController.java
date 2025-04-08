package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.models.ChuyenBay;
import com.example.banvemaybay.services.ChuyenBayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/chuyen-bay")
public class ChuyenBayAdminController {

    private final ChuyenBayService chuyenBayService;

    public ChuyenBayAdminController(ChuyenBayService chuyenBayService) {
        this.chuyenBayService = chuyenBayService;
    }
    @GetMapping("")
    public ResponseEntity<?> getListNguoiDatVe(){
        try{
            List<ChuyenBay> chuyenBayList = chuyenBayService.getAllChuyenBay();
            return ResponseEntity.ok(chuyenBayList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
