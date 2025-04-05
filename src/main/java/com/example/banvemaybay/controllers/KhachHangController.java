package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.KhachHangDTO;
import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/khach_hang")
@RequiredArgsConstructor
public class KhachHangController {

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
            @RequestBody KhachHangDTO khachHangDTO
    ) {
        return ResponseEntity.ok("tạo kh thành công " + khachHangDTO);
    }


}
