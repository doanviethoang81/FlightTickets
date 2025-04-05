package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.models.ChuyenBay;
import com.example.banvemaybay.models.PhieuDatVe;
import com.example.banvemaybay.models.ThongTinDatVe;
import com.example.banvemaybay.responses.ThongKeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhieuDatVeRepository extends JpaRepository<PhieuDatVe,Integer> {
    List<PhieuDatVe> findByIdThongTinDatVe(ThongTinDatVe idThongTinDatVe);

    ChuyenBay findByIdChuyenBay(ChuyenBay idChuyenBay);

    List<PhieuDatVe> findByNgayDat(LocalDate ngayDat);

    List<PhieuDatVe> findByNgayDatBetweenAndIdThongTinDatVe_TrangThaiThanhToan(LocalDate tuNgay, LocalDate denNgay, String trangThai);

    @Query("SELECT new com.example.banvemaybay.responses.ThongKeRepository(" +
            "COUNT(p), COUNT(DISTINCT p.idKhachHang), COALESCE(SUM(p.giaVe), 0)) " +
            "FROM PhieuDatVe p " +
            "JOIN p.idThongTinDatVe t " +
            "WHERE t.trangThaiThanhToan = 'Thành công'")
    ThongKeRepository getThongKeToanBo();

    @Query("SELECT new com.example.banvemaybay.responses.ThongKeRepository(" +
            "COUNT(p), COUNT(DISTINCT p.idKhachHang), COALESCE(SUM(p.giaVe), 0)) " +
            "FROM PhieuDatVe p " +
            "JOIN p.idThongTinDatVe t " +
            "WHERE t.trangThaiThanhToan = 'Thành công' " +
            "AND MONTH(p.ngayDat) = MONTH(:now) " +
            "AND YEAR(p.ngayDat) = YEAR(:now)")
    ThongKeRepository getThongKeTrongThang(@Param("now") LocalDate now);


}
