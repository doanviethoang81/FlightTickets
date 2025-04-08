package com.example.banvemaybay.services;

import com.example.banvemaybay.dtos.PhieuDatVeDTO;
import com.example.banvemaybay.dtos.ThongTinDatVeDTO;
import com.example.banvemaybay.models.PhieuDatVe;
import com.example.banvemaybay.responses.DoanhThuResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IPhieuDatVeService {

    PhieuDatVe createPhieuDatVe(PhieuDatVeDTO phieuDatVeDTO);

    List<PhieuDatVe> getPhieuDatVe(Integer id);

    List<PhieuDatVe> getAllPhieuDatVe();

    PhieuDatVe updateOrder(Integer id, PhieuDatVeDTO phieuDatVeDTO);

    List<ThongTinDatVeDTO> getThongTinVeTheoSdt(String sdt);

    PhieuDatVe save(PhieuDatVe phieuDatVe);

    List<PhieuDatVe> getPhieuDatTheoNgay(LocalDate phieuDat);

    double getDoanhThuDatVe(LocalDate tuNgay, LocalDate denNgay);

    DoanhThuResponse getDoanhThu(LocalDate tuNgay, LocalDate denNgay);

//    List<PhieuDatVeDTO> getThongTinVeTheoSdt(String sdt);


//    void deleteOrder(Long id);
//
//    List<PhieuDatVe> findByUserId(Long userId);
}
