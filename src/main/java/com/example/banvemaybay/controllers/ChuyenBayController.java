package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.ChuyenBayDTO;
import com.example.banvemaybay.dtos.KhachHangDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/chuyen_bay")
@RequiredArgsConstructor
public class ChuyenBayController {

    @GetMapping("")
    public ResponseEntity<String> getNguoiDat(){
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String>  timVeDaDat(
            @PathVariable("id") String id
    ){
        return ResponseEntity.ok("hello "+ id);
    }

    @PostMapping("")
    public ResponseEntity<?> taoNguoiDat(
            @RequestBody ChuyenBayDTO chuyenBayDTO
    ) {
        return ResponseEntity.ok("tạo kh thành công " + chuyenBayDTO);
    }
}
