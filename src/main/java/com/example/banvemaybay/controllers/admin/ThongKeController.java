package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.repositorys.KhachHangRepository;
import com.example.banvemaybay.repositorys.PhieuDatVeRepository;
import com.example.banvemaybay.responses.ThongKeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@RestController
@RequestMapping("${api.prefix}/admin/dashboard")
@RequiredArgsConstructor
public class ThongKeController {

    private final PhieuDatVeRepository phieuDatVeRepository;

    @PostMapping("")
    public ResponseEntity<?> getThongKe(){
        ThongKeRepository thongKe = phieuDatVeRepository.getThongKeToanBo();

        if (thongKe == null || thongKe.getSoLuongVeBan() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Collections.singletonMap("message", "Không có dữ liệu thống kê"));
        }

        return ResponseEntity.ok(thongKe);
    }

    @PostMapping("/tong-hop-thang")
    public ResponseEntity<?> getThongKeTongHopThang() {
        ThongKeRepository thongKe = phieuDatVeRepository.getThongKeTrongThang(LocalDate.now());

        if (thongKe == null || (thongKe.getSoLuongVeBan() == 0 && thongKe.getTongTien().compareTo(BigDecimal.ZERO) == 0)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Collections.singletonMap("message", "Không có dữ liệu thống kê trong tháng này"));
        }
        return ResponseEntity.ok(thongKe);
    }

}
