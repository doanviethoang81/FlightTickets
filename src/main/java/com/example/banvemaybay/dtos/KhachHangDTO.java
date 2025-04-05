package com.example.banvemaybay.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class KhachHangDTO {

    @JsonProperty("firstName")
    private String ho;

    @JsonProperty("lastName")
    private String ten;

    @JsonProperty("birthDate")
    private Date ngaySinh;

    @JsonProperty("title")
    private String danhXung;

    @JsonProperty("type")
    private String loaiKhachHang;

    @JsonProperty("number")
    private String soHoChieu;

    @JsonProperty("expirationDate")
    private Date ngayHetHan;

    @JsonProperty("issuingCountry")
    private String quocGiaCap;


}
