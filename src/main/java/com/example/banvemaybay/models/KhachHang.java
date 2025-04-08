package com.example.banvemaybay.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table( name ="khach_hang")
public class KhachHang {

    @Id // tu dong tang len 1
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ho;

    private String ten;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "danh_xung")
    private String danhXung;

    @Column(name = "loai_khach_hang", nullable = false)
    private String loaiKhachHang;

    @Column(name = "so_ho_chieu")
    private String soHoChieu;

    @Column(name = "ngay_het_han")
    private Date ngayHetHan;

    @Column(name = "quoc_gia_cap")
    private String quocGiaCap;

}
