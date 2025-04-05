package com.example.banvemaybay.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeRepository {
    private long soLuongVeBan;
    private long soLuongKhachHang;
    private BigDecimal tongTien;
}
