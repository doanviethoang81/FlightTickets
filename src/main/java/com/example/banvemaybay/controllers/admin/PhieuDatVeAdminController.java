package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.models.PhieuDatVe;
import com.example.banvemaybay.responses.DoanhThuResponse;
import com.example.banvemaybay.services.PhieuDatVeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/phieu_dat")
@RequiredArgsConstructor
public class PhieuDatVeAdminController {

    private final PhieuDatVeService phieuDatVeService;

    // list thông tin đặt vé
    @GetMapping("")
    public ResponseEntity<?> getDSPhieuDat(){
        try{
            List<PhieuDatVe> phieuDatVeList = phieuDatVeService.getAllPhieuDatVe();
            return ResponseEntity.ok(phieuDatVeList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // thống kê phiếu đặt vé theo ngày
    @GetMapping("/{ngaydat}")
    public ResponseEntity<?> getNgayDatVe(@PathVariable("ngaydat") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngaydat) {
        List<PhieuDatVe> phieuDatVeList = phieuDatVeService.getPhieuDatTheoNgay(ngaydat);
        return ResponseEntity.ok(phieuDatVeList);
    }

    //thống kê doanh thu từ ngày -- đến ngày
    @GetMapping("/doanh-thu")
    public ResponseEntity<?> getDoanhThuDatVe(
            @RequestParam("tuNgay") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tuNgay,
            @RequestParam("denNgay") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate denNgay
            ) {
        DoanhThuResponse doanhThu = phieuDatVeService.getDoanhThu(tuNgay, denNgay);
        return ResponseEntity.ok(doanhThu);
    }

}
// người dùng doanh thu số vé