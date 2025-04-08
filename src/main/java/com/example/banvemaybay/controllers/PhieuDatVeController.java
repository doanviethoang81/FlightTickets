package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.PhieuDatVeDTO;
import com.example.banvemaybay.models.PhieuDatVe;
import com.example.banvemaybay.services.PhieuDatVeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/phieu_dat_ve")
public class PhieuDatVeController {
    private final PhieuDatVeService phieuDatVeService;

    @GetMapping("")
    public ResponseEntity<?> getNguoiDat(){
        List<PhieuDatVe>  phieuDatVeList = phieuDatVeService.getAllPhieuDatVe();
        return ResponseEntity.ok(phieuDatVeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  timVeDaDat(
            @PathVariable("id") Integer id
    ){
       try{
//            PhieuDatVe phieuDatVe = phieuDatVeService.getPhieuDatVe(id);
//            return ResponseEntity.ok(phieuDatVe);
           return ResponseEntity.ok("oke");
       } catch (Exception e) {

           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
    }

    @PostMapping("")
    public ResponseEntity<?> taoNguoiDat(
            @RequestBody PhieuDatVeDTO phieuDatVeDTO
    ) {
        return ResponseEntity.ok("tạo kh thành công " + phieuDatVeDTO);
    }
}
