package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.ThongTinDatVe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThongTinDatVeRepository extends JpaRepository<ThongTinDatVe,Integer> {
    List<ThongTinDatVe> findByIdNguoiDat(NguoiDat nguoiDat);
}
