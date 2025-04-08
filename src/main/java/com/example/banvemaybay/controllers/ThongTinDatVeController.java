package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.KhachHangDTO;
import com.example.banvemaybay.dtos.ThongTinDatVeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/thong_tin_dat_ve")
@RequiredArgsConstructor
public class ThongTinDatVeController {

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
            @RequestBody ThongTinDatVeDTO thongTinDatVeDTO
    ) {
        return ResponseEntity.ok("tạo kh thành công " + thongTinDatVeDTO);
    }
}
