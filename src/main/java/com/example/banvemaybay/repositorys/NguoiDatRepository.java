package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.dtos.UserRoleAdminDTO;
import com.example.banvemaybay.models.NguoiDat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NguoiDatRepository extends JpaRepository<NguoiDat, Integer> {
    Optional<NguoiDat> findBySoDienThoai(String sdt);
    Optional<NguoiDat> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT new com.example.banvemaybay.dtos.UserRoleAdminDTO(u.id,u.ho,u.ten,u.soDienThoai,u.email,u.enable,u.provider) FROM NguoiDat u JOIN u.roles r WHERE r.name = :roleName")
    List<UserRoleAdminDTO> findUsersByRoleName(@Param("roleName") String rolename);
}
