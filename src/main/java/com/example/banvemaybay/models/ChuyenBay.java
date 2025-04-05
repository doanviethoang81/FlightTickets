package com.example.banvemaybay.models;

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
@Table( name ="chuyen_bay")
public class ChuyenBay {

    @Id // tu dong tang len 1
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "san_bay_di")
    private String sanBayDi;

    @Column(name = "san_bay_den")
    private String sanBayDen;

    @Column(name = "ngay_gio_di")
    private Date ngayGioDi;

    @Column(name = "ngay_gio_den")
    private Date ngayGioDen;

    @Column(name = "hang_bay")
    private String hangBay;
}
