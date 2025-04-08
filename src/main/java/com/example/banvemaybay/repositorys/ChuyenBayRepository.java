package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.models.ChuyenBay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ChuyenBayRepository extends JpaRepository<ChuyenBay,Integer> {
    boolean existsBySanBayDiAndSanBayDenAndNgayGioDiAndNgayGioDenAndHangBay(
            String sanBayDi, String sanBayDen, Date ngayGioDi,Date ngayGioDen, String hangBay);

    Optional<ChuyenBay> findBySanBayDiAndSanBayDenAndNgayGioDiAndNgayGioDenAndHangBay(
            String sanBayDi, String sanBayDen, Date ngayGioDi,Date ngayGioDen, String hangBay);
}
