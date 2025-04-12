package com.example.banvemaybay.services;

import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.UserRoleAdminDTO;
import com.example.banvemaybay.models.NguoiDat;

import java.util.List;

public interface INguoiDatService {
    NguoiDatDTO timVeDaDatTheoGmail(String sdt);

    NguoiDat save(NguoiDat nguoiDat);

    List<NguoiDat> getAllNguoiDat();

    List<UserRoleAdminDTO> getAllNguoiDatByRoleName(String rolename);
}
