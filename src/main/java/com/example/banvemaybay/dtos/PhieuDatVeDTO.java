package com.example.banvemaybay.dtos;

import com.example.banvemaybay.models.ThongTinDatVe;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"ma_ve", "gia_ve", "danhSachKhachHang"})
public class PhieuDatVeDTO {

    @JsonProperty("ticketCode")
    private String maVe;
    @JsonProperty("priceTicket")
    private BigDecimal giaVe;

//    @JsonProperty("thing_tin_khach_hang")
    @JsonProperty("passengers")
    private KhachHangDTO khachHang; // Danh sách vé của khách hàng
}

//    @JsonProperty("id_thong_tin_dat_ve")
//    private int idThongTinDatVe;
//
//    @JsonProperty("id_khach_hang")
//    private int idKhachHang;
//
//    @JsonProperty("id_chuyen_bay")
//    private int idChuyenBay;

//    @JsonProperty("ngay_dat")
//    private Date ngayDat;
//
//    @JsonProperty("ngay_bay")
//    private Date ngayBay;
//
//    @JsonProperty("ngay_ve")
//    private Date ngayVe;
//
//    @JsonProperty("hang_ve")
//    private String hangVe;
//


//    private ChuyenBayDTO chuyenBay;  // Thông tin chuyến bay
//    private ThongTinDatVeDTO thongTinDatVe;