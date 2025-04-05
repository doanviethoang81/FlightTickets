package com.example.banvemaybay.dtos;

import com.example.banvemaybay.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatVeDTO {

    private String classType;
    private NguoiDat nguoiDat; // ContactInfo
    private PhieuDatVe phieuDatVe;//flightTickets
    private ChuyenBay chuyenBay;
    private List<KhachHang> khachHangs;//passengers
}
