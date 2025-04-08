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
@RequestMapping("${api.prefix:/api/v1}/phieu_dat_ve")
public class PhieuDatVeController {
    private final PhieuDatVeService phieuDatVeService;

    @GetMapping("")
    public ResponseEntity<?> getPhieuDatVe(){
        List<PhieuDatVe>  phieuDatVeList = phieuDatVeService.getAllPhieuDatVe();
        return ResponseEntity.ok(phieuDatVeList);
    }

}
