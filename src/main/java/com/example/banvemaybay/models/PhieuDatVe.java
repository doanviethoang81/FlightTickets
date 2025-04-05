package com.example.banvemaybay.models;

import com.example.banvemaybay.dtos.KhachHangDTO;
import com.example.banvemaybay.dtos.PhieuDatVeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table( name ="phieu_dat_ve")
public class PhieuDatVe {

    @Id // tu dong tang len 1
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ma_ve")
    private String maVe;

    @ManyToOne
    @JoinColumn(name = "id_thong_tin_dat_ve")
    private ThongTinDatVe idThongTinDatVe;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne
    @JoinColumn(name = "id_chuyen_bay")
    private ChuyenBay idChuyenBay;

    @Column(name = "ngay_dat")
    private LocalDate ngayDat;

    @Column(name = "hang_ve")
    private String hangVe;

    @Column(name = "gia_ve")
    private BigDecimal giaVe;

}
