package com.example.banvemaybay.services;

import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.ThongTinDatVeChiTietDTO;
import com.example.banvemaybay.dtos.ThongTinDatVeDTO;
import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.ThongTinDatVe;

import java.util.List;
import java.util.Optional;

public interface IThongTinDatVeService {
    ThongTinDatVe createThongTinDatVe(ThongTinDatVeDTO thongTinDatVeDTO);

    ThongTinDatVe getThongTinDatVe(NguoiDat nguoiDat);

    List<ThongTinDatVe> getAllThongTinDatVe();

    ThongTinDatVe updateThongTinDatVe(Integer id, ThongTinDatVeDTO thongTinDatVeDTO);

    ThongTinDatVe save(ThongTinDatVe thongTinDatVe);

    void updateTrangThaiThanhToan(Integer id, String trangThai);

    Optional<ThongTinDatVe> getThongTinDatVeId(Integer id);

    List<ThongTinDatVeChiTietDTO> listThongTinDatVe();
}
