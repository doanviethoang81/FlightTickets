package com.example.banvemaybay.dtos;

import com.example.banvemaybay.models.ThongTinDatVe;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"firstName", "lastName", "phone", "email", "danhSachThongTinDatVe"})
public class NguoiDatDTO {

    @JsonProperty("firstName")
    private String ho;

    @JsonProperty("lastName")
    private String ten;

    @JsonProperty("phone")
    private String soDienThoai;

    @JsonProperty("email")
    private String email;

    @JsonProperty("ticketBookingList")
    private List<ThongTinDatVeDTO> danhSachThongTinDatVe;
}
