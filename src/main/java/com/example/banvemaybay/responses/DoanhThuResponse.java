package com.example.banvemaybay.responses;

import com.example.banvemaybay.models.PhieuDatVe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoanhThuResponse {

    private String tongDoanhThu;
    private List<PhieuDatVe> danhSachPhieuDat;
}
