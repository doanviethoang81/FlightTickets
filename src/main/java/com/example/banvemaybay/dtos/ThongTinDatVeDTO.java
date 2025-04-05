package com.example.banvemaybay.dtos;

import com.example.banvemaybay.models.ChuyenBay;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongTinDatVeDTO {

    @JsonProperty("paymentMethod ")
    private String loaiThanhToan;

    @JsonProperty("status")
    private String trangThai;

    @JsonProperty("totalMoney")
    private BigDecimal tongTien;

//    @JsonProperty("nguoiDat")
//    private NguoiDatDTO nguoiDat;
    @JsonProperty("bookingDate")
    private LocalDate ngayDat;

    @JsonProperty("ticketClass")
    private String hangVe;

    @JsonProperty("flight")
    private ChuyenBayDTO chuyenBay;

    @JsonProperty("customerList")
    private List<PhieuDatVeDTO> danhSachKhachHang;

}
