package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.models.ThongTinDatVe;
import com.example.banvemaybay.services.*;
import com.example.banvemaybay.utils.NumberEncryptor;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("${api.prefix}/nguoi_dat")
@RequiredArgsConstructor
public class NguoiDatController {

    private final NguoiDatService nguoiDatService;
    private final ThongTinDatVeService thongTinDatVeService;
    private final PhieuDatVeService phieuDatVeService;
    private final KhachHangService khachHangService;
    private final ChuyenBayService chuyenBayService;

    //tìm người đặt sdt
    @GetMapping("/{sdt}")
    public ResponseEntity<?> timVeTheoSdtNguoiDat(
            @PathVariable("sdt") String sdt
    ){
        try{
//            List<PhieuDatVeDTO> phieuDatVeDTOList = phieuDatVeService.getThongTinVeTheoSdt(sdt);
            NguoiDatDTO nguoiDatDTO = nguoiDatService.timVeDaDatTheoSDT(sdt);
//            List<ThongTinDatVeDTO> listThongTinDatVeDTO = phieuDatVeService.getThongTinVeTheoSdt(sdt);
//            NguoiDat nguoiDat = nguoiDatService.timVeDaDatTheoSDT(sdt);
//            ThongTinDatVe thongTinDatVe= thongTinDatVeService.getThongTinDatVe(nguoiDat);
//            List<PhieuDatVe> phieuDatVeList = phieuDatVeService.getPhieuDatVe(thongTinDatVe.getId());
            return ResponseEntity.ok(nguoiDatDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<String> getNguoiDat(){
        return ResponseEntity.ok("hello");
    }

    //đặt vé
    @PostMapping("/dat-ve")
    public ResponseEntity<?> datVe(@RequestBody JsonNode json) {
        try {
            ThongTinDatVe thongTinDatVe= nguoiDatService.xuLyDatVe(json);
            if (thongTinDatVe != null) {
                double totalMoney = thongTinDatVe.getTongTien().doubleValue();
                int orderId = thongTinDatVe.getId();

                String encryptedId = NumberEncryptor.encryptId(orderId);
                String redirectUrl = "http://localhost:8080/api/v1/payment/vn-pay?amount=" + (long)(totalMoney * 100) + "&id=" + encryptedId;

                // Tạo trang HTML có script chuyển hướng
                String htmlContent = "<html><head>"
                        + "<meta http-equiv='refresh' content='0;url=" + redirectUrl + "'/>"
                        + "<script>window.location.href = '" + redirectUrl + "';</script>"
                        + "</head><body>"
                        + "Nếu bạn không được chuyển hướng, <a href='" + redirectUrl + "'>bấm vào đây</a>."
                        + "</body></html>";

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "text/html")
                        .body(htmlContent);
            } else {
                return ResponseEntity.ok("Đặt vé thất bại");
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi xử lý dữ liệu: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateNguoidat(
            @PathVariable("id") int id
    ){
        return ResponseEntity.ok("sửa thông tin thành công "+ id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNguoidat(
            @PathVariable("id") int id
    ){
        return ResponseEntity.ok("xóa thành cong "+ id);
    }


}
