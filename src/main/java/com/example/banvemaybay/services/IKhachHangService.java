package com.example.banvemaybay.services;

import com.example.banvemaybay.models.KhachHang;

import java.util.List;

public interface IKhachHangService {
    KhachHang save(KhachHang khachHang);
    List<KhachHang> saveAll(List<KhachHang> khachHangs);
    List<KhachHang> getAllKhachHang();
}
