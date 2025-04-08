package com.example.banvemaybay.services;

import com.example.banvemaybay.models.KhachHang;
import com.example.banvemaybay.repositorys.KhachHangRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KhachHangService implements IKhachHangService{

    private final KhachHangRepository khachHangRepository;
    @Override
    public KhachHang save(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    @Override
    public List<KhachHang> saveAll(List<KhachHang> khachHangs) {
        return (List<KhachHang>) khachHangRepository.saveAll(khachHangs);
    }

    @Override
    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }
}
