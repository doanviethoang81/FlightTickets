package com.example.banvemaybay.services;

import com.example.banvemaybay.dtos.*;
import com.example.banvemaybay.models.*;
import com.example.banvemaybay.repositorys.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThongTinDatVeService implements IThongTinDatVeService{

    private final ThongTinDatVeRepository thongTinDatVeRepository;
    private final NguoiDatRepository nguoiDatRepository;
    private final PhieuDatVeRepository phieuDatVeRepository;
    private final ChuyenBayRepository chuyenBayRepository;
    private final KhachHangRepository khachHangRepository;

    @Override
    public ThongTinDatVe createThongTinDatVe(ThongTinDatVeDTO thongTinDatVeDTO) {
        return null;
    }

    @Override
    public ThongTinDatVe getThongTinDatVe(NguoiDat nguoiDat) {
        return null;
    }

    @Override
    public List<ThongTinDatVe> getAllThongTinDatVe() {
        return thongTinDatVeRepository.findAll();
    }

    @Override
    public ThongTinDatVe updateThongTinDatVe(Integer id, ThongTinDatVeDTO thongTinDatVeDTO) {
        return null;
    }

    @Override
    public ThongTinDatVe save(ThongTinDatVe thongTinDatVe) {
        return thongTinDatVeRepository.save(thongTinDatVe);
    }

    @Override
    public void updateTrangThaiThanhToan(Integer id, String trangThai) {
        Optional<ThongTinDatVe> existingThongTinDatVe = thongTinDatVeRepository.findById(id);
        if(existingThongTinDatVe.isPresent()){
            ThongTinDatVe thongTinDatVe =existingThongTinDatVe.get();
            thongTinDatVe.setTrangThaiThanhToan(trangThai);
            thongTinDatVeRepository.save(thongTinDatVe);
        }
    }

    @Override
    public Optional<ThongTinDatVe> getThongTinDatVeId(Integer id) {
        return thongTinDatVeRepository.findById(id);
    }

    @Override
    public List<ThongTinDatVeChiTietDTO> listThongTinDatVe() {
        // Lấy tất cả người đặt vé
        List<ThongTinDatVe> thongTinDatVeList = thongTinDatVeRepository.findAll();
        List<ThongTinDatVeChiTietDTO> thongTinDatVeDTOList = new ArrayList<>();

        for (ThongTinDatVe thongTinDatVe : thongTinDatVeList) {
            NguoiDat nguoiDat = nguoiDatRepository.findById(thongTinDatVe.getIdNguoiDat().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người đặt với ID: " + thongTinDatVe.getIdNguoiDat().getId()));

            List<PhieuDatVe> danhSachPhieuDatVe = phieuDatVeRepository.findByIdThongTinDatVe(thongTinDatVe)
                    .stream()
                    .sorted(Comparator.comparing(PhieuDatVe::getNgayDat).reversed()) // Sắp xếp giảm dần theo ngày đặt
                    .collect(Collectors.toList());

            if (danhSachPhieuDatVe.isEmpty()) {
                continue;
            }

            // Chuyển đổi danh sách phiếu đặt vé sang danh sách DTO
            List<PhieuDatVeDTO> danhSachPhieuDatVeDTO = danhSachPhieuDatVe.stream().map(phieu -> {
                KhachHang khachHang = khachHangRepository.findById(phieu.getIdKhachHang().getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + phieu.getIdKhachHang().getId()));

                KhachHangDTO khachHangDTO = KhachHangDTO.builder()
                        .ho(khachHang.getHo())
                        .ten(khachHang.getTen())
                        .ngaySinh(khachHang.getNgaySinh())
                        .danhXung(khachHang.getDanhXung())
                        .loaiKhachHang(khachHang.getLoaiKhachHang())
                        .soHoChieu(khachHang.getSoHoChieu())
                        .ngayHetHan(khachHang.getNgayHetHan())
                        .quocGiaCap(khachHang.getQuocGiaCap())
                        .build();

                return PhieuDatVeDTO.builder()
                        .maVe(phieu.getMaVe())
                        .giaVe(phieu.getGiaVe())
                        .khachHang(khachHangDTO)
                        .build();
            }).collect(Collectors.toList());

            // Chuyển đổi danh sách chuyến bay sang danh sách DTO
            List<ChuyenBayDTO> danhSachChuyenBayDTO = danhSachPhieuDatVe.stream()
                    .map(phieu -> {
                        ChuyenBay chuyenBay = chuyenBayRepository.findById(phieu.getIdChuyenBay().getId())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến bay với ID: " + phieu.getIdChuyenBay().getId()));

                        return ChuyenBayDTO.builder()
                                .sanBayDi(chuyenBay.getSanBayDi())
                                .sanBayDen(chuyenBay.getSanBayDen())
                                .ngayGioDi(chuyenBay.getNgayGioDi())
                                .ngayGioDen(chuyenBay.getNgayGioDen())
                                .hangBay(chuyenBay.getHangBay())
                                .build();
                    })
                    .distinct()
                    .collect(Collectors.toList());

            // Tạo DTO cuối cùng
            ThongTinDatVeChiTietDTO dto = new ThongTinDatVeChiTietDTO();
            dto.setHo(nguoiDat.getHo());
            dto.setTen(nguoiDat.getTen());
            dto.setSoDienThoai(nguoiDat.getSoDienThoai());
            dto.setEmail(nguoiDat.getEmail()); // Sửa lỗi email
            dto.setNgayDat(danhSachPhieuDatVe.get(0).getNgayDat()); // Lấy ngày đặt mới nhất
            dto.setHangVe(danhSachPhieuDatVe.get(0).getHangVe());
            dto.setLoaiThanhToan(thongTinDatVe.getLoaiThanhToan());
            dto.setTrangThaiThanhToan(thongTinDatVe.getTrangThaiThanhToan());
            dto.setTongTien(thongTinDatVe.getTongTien());
            dto.setPhieuDatVeDTOS(danhSachPhieuDatVeDTO);
            dto.setChuyenBayDTO(danhSachChuyenBayDTO);

            thongTinDatVeDTOList.add(dto);
        }

        // Sắp xếp danh sách thông tin đặt vé theo thời gian đặt mới nhất
        thongTinDatVeDTOList.sort(Comparator.comparing(ThongTinDatVeChiTietDTO::getNgayDat).reversed());

        return thongTinDatVeDTOList;
    }


}
