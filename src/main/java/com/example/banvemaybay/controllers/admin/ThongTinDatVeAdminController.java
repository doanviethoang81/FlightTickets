package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.ThongTinDatVeChiTietDTO;
import com.example.banvemaybay.models.PhieuDatVe;
import com.example.banvemaybay.models.ThongTinDatVe;
import com.example.banvemaybay.services.PhieuDatVeService;
import com.example.banvemaybay.services.ThongTinDatVeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/admin/thong_tin_dat_ve")
@RequiredArgsConstructor
public class ThongTinDatVeAdminController {

    private final ThongTinDatVeService thongTinDatVeService;
    private final PhieuDatVeService phieuDatVeService;

    @GetMapping("")
    public ResponseEntity<?> getThongTinDatVe(){
        List<ThongTinDatVeChiTietDTO> thongTinDatVeList = thongTinDatVeService.listThongTinDatVe();
        return ResponseEntity.ok(thongTinDatVeList);
    }

    // chi tiết của 1 vé
    @GetMapping("/{id}")
    public ResponseEntity<?> getThongTinChiTietDatVe(@PathVariable("id") Integer id){
        Optional<ThongTinDatVe> thongTinDatVe = thongTinDatVeService.getThongTinDatVeId(id);
        List<PhieuDatVe> phieuDatVe = phieuDatVeService.getPhieuDatVe(thongTinDatVe.get().getId());
        return ResponseEntity.ok(phieuDatVe);
    }


}
