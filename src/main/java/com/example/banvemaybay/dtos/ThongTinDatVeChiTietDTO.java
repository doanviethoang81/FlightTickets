package com.example.banvemaybay.dtos;

import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.PhieuDatVe;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"firstName", "lastName", "phone", "email","bookingDate","ticketClass"
        ,"paymentType","paymentStatus","total","flight","ticketBookingList"})
public class ThongTinDatVeChiTietDTO {

    @JsonProperty("firstName")
    private String ho;

    @JsonProperty("lastName")
    private String ten;

    @JsonProperty("phone")
    private String soDienThoai;

    @JsonProperty("email")
    private String email;

    @JsonProperty("bookingDate")
    private LocalDate ngayDat;

    @JsonProperty("ticketClass")
    private String hangVe;

    @JsonProperty("paymentType")
    private String loaiThanhToan;

    @JsonProperty("paymentStatus")
    private String trangThaiThanhToan;

    @JsonProperty("total")
    private BigDecimal tongTien;

    @JsonProperty("flight")
    private List<ChuyenBayDTO> chuyenBayDTO;

    @JsonProperty("ticketBookingList")
    private List<PhieuDatVeDTO> phieuDatVeDTOS;

}
