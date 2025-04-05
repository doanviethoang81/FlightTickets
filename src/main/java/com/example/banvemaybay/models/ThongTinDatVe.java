package com.example.banvemaybay.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table( name ="thong_tin_dat_ve")
public class ThongTinDatVe {

    @Id // tu dong tang len 1
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dat")
    private NguoiDat idNguoiDat;

    @Column(name = "loai_thanh_toan")
    private String loaiThanhToan;

    @Column(name = "trang_thai_thanh_toan")
    private String trangThaiThanhToan;

    @Column(name = "tong_tien", nullable = false)
    private BigDecimal tongTien;
}
