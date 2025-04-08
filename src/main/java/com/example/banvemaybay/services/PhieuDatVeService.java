package com.example.banvemaybay.services;

import com.example.banvemaybay.dtos.*;
import com.example.banvemaybay.models.*;
import com.example.banvemaybay.repositorys.*;
import com.example.banvemaybay.responses.DoanhThuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhieuDatVeService implements IPhieuDatVeService{

    private  final PhieuDatVeRepository phieuDatVeRepository;
    private  final ThongTinDatVeRepository thongTinDatVeRepository;
    private final NguoiDatRepository nguoiDatRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChuyenBayRepository chuyenBayRepository;

    @Override
    public PhieuDatVe createPhieuDatVe(PhieuDatVeDTO phieuDatVeDTO) {
        return null;
    }

    @Override
    public List<PhieuDatVe> getPhieuDatVe(Integer idThongTinDatVe) {
        ThongTinDatVe thongTinDatVe = thongTinDatVeRepository.findById(idThongTinDatVe)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin đặt vé!"));

        return phieuDatVeRepository.findByIdThongTinDatVe(thongTinDatVe);
    }

    @Override
    public List<PhieuDatVe> getAllPhieuDatVe() {
        List<PhieuDatVe> danhSach = phieuDatVeRepository.findAll();

        if (danhSach.isEmpty()) {
            throw new RuntimeException("Không có dữ liệu.");
        }
        return danhSach;
    }

    @Override
    public PhieuDatVe updateOrder(Integer id, PhieuDatVeDTO phieuDatVeDTO) {
        return null;
    }

    @Override
    public List<ThongTinDatVeDTO> getThongTinVeTheoSdt(String sdt) {
        // Tìm thông tin người đặt theo số điện thoại
        NguoiDat nguoiDat = nguoiDatRepository.findBySoDienThoai(sdt)
                .orElseThrow(() -> new RuntimeException("Không tìm vé của người đặt với sđt! "+ sdt ));

        // Lấy danh sách thông tin đặt vé của người này
        List<ThongTinDatVe> danhSachThongTinDatVe = thongTinDatVeRepository.findByIdNguoiDat(nguoiDat);

        List<ThongTinDatVeDTO> result = new ArrayList<>();

        for (ThongTinDatVe thongTin : danhSachThongTinDatVe) {
            // Lấy danh sách phiếu đặt vé của từng thông tin đặt vé
            List<PhieuDatVe> danhSachPhieuDatVe = phieuDatVeRepository.findByIdThongTinDatVe(thongTin);

            // Chuyển danh sách phiếu đặt vé sang DTO
            List<PhieuDatVeDTO> danhSachPhieuDatVeDTO = danhSachPhieuDatVe.stream().map(phieu -> {
                // Lấy thông tin khách hàng theo ID
                Integer idKhachHang = phieu.getIdKhachHang().getId();
                Integer idChuyenBay = phieu.getIdChuyenBay().getId();
                KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));
                ChuyenBay chuyenBay = chuyenBayRepository.findById(idChuyenBay)
                        .orElseThrow(()-> new RuntimeException("Không tìm thấy chuyến bay!"));

                // Chuyển đổi KhachHang thành DTO bằng @Builder
                KhachHangDTO khachHangDTO = KhachHangDTO.builder()
//                        .idKhachHang(khachHang.getIdKhachHang())
                        .ho(khachHang.getHo())
                        .ten(khachHang.getTen())
                        .ngaySinh(khachHang.getNgaySinh())
                        .danhXung(khachHang.getDanhXung())
                        .loaiKhachHang(khachHang.getLoaiKhachHang())
                        .build();
                ChuyenBayDTO chuyenBayDTO = ChuyenBayDTO.builder()
                        .sanBayDi(chuyenBay.getSanBayDi())
                        .sanBayDen(chuyenBay.getSanBayDen())
                        .ngayGioDi(chuyenBay.getNgayGioDi())
                        .ngayGioDen(chuyenBay.getNgayGioDen())
                        .hangBay(chuyenBay.getHangBay())
                        .build();

                return PhieuDatVeDTO.builder()
                        .maVe(phieu.getMaVe())
                        .giaVe(phieu.getGiaVe())
//                        .idThongTinDatVe(phieu.getIdThongTinDatVe())
//                        .idKhachHang(phieu.getIdKhachHang())
//                        .idChuyenBay(phieu.getIdChuyenBay())
//                        .ngayDat(phieu.getNgayDat())
//                        .ngayBay(phieu.getNgayBay())
//                        .ngayVe(phieu.getNgayVe())
//                        .hangVe(phieu.getHangVe())
//                        .giaVe(phieu.getGiaVe())
                        .khachHang(khachHangDTO) // Thêm thông tin khách hàng vào DTO
//                        .chuyenBay(chuyenBayDTO)
                        .build();
            }).collect(Collectors.toList());
//            NguoiDatDTO nguoiDatDTO = NguoiDatDTO.builder()
//                    .ho(nguoiDat.getHo())
//                    .ten(nguoiDat.getTen())
//                    .soDienThoai(nguoiDat.getSoDienThoai())
//                    .email(nguoiDat.getEmail())
//                    .build();

            // Tạo DTO cho thông tin đặt vé bằng @Builder
            ThongTinDatVeDTO thongTinDatVeDTO = ThongTinDatVeDTO.builder()
                    .loaiThanhToan(thongTin.getLoaiThanhToan())
                    .trangThai(thongTin.getTrangThaiThanhToan())
                    .tongTien(thongTin.getTongTien())
//                    .nguoiDat(nguoiDatDTO)
                    .danhSachKhachHang(danhSachPhieuDatVeDTO) // Gán danh sách vé vào DTO
                    .build();
            result.add(thongTinDatVeDTO);
        }

        return result;
    }

    @Override
    public PhieuDatVe save(PhieuDatVe phieuDatVe) {
        return phieuDatVeRepository.save(phieuDatVe);
    }

    @Override
    public List<PhieuDatVe> getPhieuDatTheoNgay(LocalDate phieuDat) {
         List<PhieuDatVe> phieuDatVe = phieuDatVeRepository.findByNgayDat(phieuDat);
        return phieuDatVe;
    }

    @Override
    public double getDoanhThuDatVe(LocalDate tuNgay, LocalDate denNgay) {
        List<PhieuDatVe> danhSachPhieu = phieuDatVeRepository.findByNgayDatBetweenAndIdThongTinDatVe_TrangThaiThanhToan(tuNgay, denNgay, "Thành công");
        return danhSachPhieu.stream()
                .mapToDouble(phieu -> phieu.getIdThongTinDatVe().getTongTien().doubleValue())
                .sum();
    }

    @Override
    public DoanhThuResponse getDoanhThu(LocalDate tuNgay, LocalDate denNgay) {
        List<PhieuDatVe> danhSachPhieu = phieuDatVeRepository.findByNgayDatBetweenAndIdThongTinDatVe_TrangThaiThanhToan(tuNgay, denNgay, "Thành công");
        Double tongDoanhThu = danhSachPhieu.stream()
                .mapToDouble(phieu -> phieu.getIdThongTinDatVe().getTongTien().doubleValue())
                .sum();
        String tongDoanhThuFormat= new BigDecimal(tongDoanhThu).toPlainString();
        return new  DoanhThuResponse(tongDoanhThuFormat,danhSachPhieu);
    }


}
