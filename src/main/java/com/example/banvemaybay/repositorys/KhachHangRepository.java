package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.models.KhachHang;
import com.example.banvemaybay.models.NguoiDat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

//    List<KhachHang> findByIdIn(List<Integer> idKhachHangList);
//    KhachHang finByIdKhachHang(Integer id);
    Optional<KhachHang> findBySoHoChieu(String soHoChieu);

    long count();

}
