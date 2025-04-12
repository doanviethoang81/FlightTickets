package com.example.banvemaybay.services;

import com.example.banvemaybay.dtos.*;
import com.example.banvemaybay.models.*;
import com.example.banvemaybay.repositorys.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NguoiDatService implements INguoiDatService{

    private  final PhieuDatVeRepository phieuDatVeRepository;
    private  final ThongTinDatVeRepository thongTinDatVeRepository;
    private final NguoiDatRepository nguoiDatRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChuyenBayRepository chuyenBayRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepostiory roleRepostiory;;

    @Override
    public NguoiDatDTO timVeDaDatTheoGmail(String gmail) {
        // Tìm người đặt theo gmail
        NguoiDat nguoiDat = nguoiDatRepository.findByEmail(gmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé của người đặt với gmail: " + gmail));

        // Lấy danh sách thông tin đặt vé của người này
        List<ThongTinDatVe> danhSachThongTinDatVe = thongTinDatVeRepository.findByIdNguoiDat(nguoiDat);

        // Chuyển đổi danh sách thông tin đặt vé sang DTO
        List<ThongTinDatVeDTO> result = new ArrayList<>();

        for (ThongTinDatVe thongTin : danhSachThongTinDatVe) {
            // Lấy danh sách phiếu đặt vé của từng thông tin đặt vé
            List<PhieuDatVe> danhSachPhieuDatVe = phieuDatVeRepository.findByIdThongTinDatVe(thongTin);

            // Nếu không có phiếu đặt vé, bỏ qua thông tin này
            if (danhSachPhieuDatVe.isEmpty()) {
                continue;
            }

            // Lấy thông tin từ phiếu đặt vé đầu tiên để gán các thông tin chung
            PhieuDatVe phieuDauTien = danhSachPhieuDatVe.get(0);

            // Lấy thông tin chuyến bay
            ChuyenBay chuyenBay = chuyenBayRepository.findById(phieuDauTien.getIdChuyenBay().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến bay với ID: " + phieuDauTien.getIdChuyenBay().getId()));

            // Chuyển đổi ChuyenBay thành DTO
            ChuyenBayDTO chuyenBayDTO = ChuyenBayDTO.builder()
                    .sanBayDi(chuyenBay.getSanBayDi())
                    .sanBayDen(chuyenBay.getSanBayDen())
                    .ngayGioDi(chuyenBay.getNgayGioDi())
                    .ngayGioDen(chuyenBay.getNgayGioDen())
                    .hangBay(chuyenBay.getHangBay())
                    .build();

            // Chuyển đổi danh sách phiếu đặt vé sang DTO, bao gồm mã vé riêng cho từng khách hàng
            List<PhieuDatVeDTO> danhSachPhieuDatVeDTO = danhSachPhieuDatVe.stream().map(phieu -> {
                // Lấy thông tin khách hàng
                KhachHang khachHang = khachHangRepository.findById(phieu.getIdKhachHang().getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + phieu.getIdKhachHang().getId()));

                // Chuyển đổi KhachHang thành DTO
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

                // Tạo PhieuDatVeDTO với mã vé riêng
                return PhieuDatVeDTO.builder()
                        .maVe(phieu.getMaVe()) // Gán mã vé từ PhieuDatVe
                        .giaVe(phieu.getGiaVe())
                        .khachHang(khachHangDTO)
                        .build();
            }).collect(Collectors.toList());

            // Tạo ThongTinDatVeDTO với thông tin chung
            ThongTinDatVeDTO thongTinDatVeDTO = ThongTinDatVeDTO.builder()
                    .loaiThanhToan(thongTin.getLoaiThanhToan())
                    .trangThai(thongTin.getTrangThaiThanhToan())
                    .tongTien(thongTin.getTongTien())
                    .ngayDat(phieuDauTien.getNgayDat())
                    .hangVe(phieuDauTien.getHangVe())
                    .chuyenBay(chuyenBayDTO)
                    .danhSachKhachHang(danhSachPhieuDatVeDTO) // Danh sách chứa mã vé và khách hàng
                    .build();

            result.add(thongTinDatVeDTO);
        }
        result.sort((a, b) -> b.getNgayDat().compareTo(a.getNgayDat()));

        // Chuyển đổi NguoiDat thành DTO và gán danh sách thông tin đặt vé
        NguoiDatDTO nguoiDatDTO = NguoiDatDTO.builder()
                .ho(nguoiDat.getHo())
                .ten(nguoiDat.getTen())
                .soDienThoai(nguoiDat.getSoDienThoai())
                .email(nguoiDat.getEmail())
                .danhSachThongTinDatVe(result)
                .build();

        return nguoiDatDTO;
    }

    @Override
    public NguoiDat save(NguoiDat nguoiDat) {
        return nguoiDatRepository.save(nguoiDat);
    }

    @Override
    public List<NguoiDat> getAllNguoiDat() {
        return nguoiDatRepository.findAll();
    }

    @Override
    public List<UserRoleAdminDTO> getAllNguoiDatByRoleName(String rolename) {
        return nguoiDatRepository.findUsersByRoleName(rolename);

    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public ThongTinDatVe xuLyDatVe(JsonNode json) throws Exception {

        if (json == null || !json.has("contactInfo") || !json.has("flightTickets")) {
            throw new IllegalArgumentException("Dữ liệu đặt vé không hợp lệ");
        }
        JsonNode contactInfo = json.get("contactInfo");
        JsonNode flightTickets = json.get("flightTickets");
        JsonNode departure = flightTickets.get("departure");
        JsonNode returnFlight = flightTickets.get("return");
        String classType = json.get("classType").asText();
        String totalPrice = json.get("total").asText();
        double totalPriceFrommat = Double.parseDouble(totalPrice);

        NguoiDat nguoiDat = luuNguoiDat(contactInfo);
        ChuyenBay chuyenBayDi = luuChuyenBay(departure);
        ThongTinDatVe thongTinDatVe = luuThongTinDatVe(nguoiDat,totalPriceFrommat);

        // Nếu có vé khứ hồi, lưu thêm 1 chuyến bay nữa
        if (returnFlight != null && !returnFlight.isNull()) {
            ChuyenBay chuyenBayVe = luuChuyenBay(returnFlight);
            luuDanhSachPhieuDatVe(json.get("passengers"), thongTinDatVe, chuyenBayVe, classType);
        }

        // Lưu danh sách phiếu đặt vé cho chuyến bay đi
        luuDanhSachPhieuDatVe(json.get("passengers"), thongTinDatVe, chuyenBayDi, classType);

        return thongTinDatVe;
    }

    private NguoiDat luuNguoiDat(JsonNode contactInfo) {
        String email = contactInfo.get("email").asText();

        // Kiểm tra nếu email đã tồn tại
        Optional<NguoiDat> nguoiDatOptional = nguoiDatRepository.findByEmail(email);

        if (nguoiDatOptional.isPresent()) {
            // Nếu tồn tại, cập nhật thông tin mới
            NguoiDat nguoiDat = nguoiDatOptional.get();
            nguoiDat.setHo(contactInfo.get("firstName").asText());
            nguoiDat.setTen(contactInfo.get("lastName").asText());
            nguoiDat.setSoDienThoai(contactInfo.get("phone").asText());
            return nguoiDatRepository.save(nguoiDat);
        } else {

            // Tạo mới nếu chưa tồn tại
            NguoiDat nguoiDat = new NguoiDat();
            nguoiDat.setHo(contactInfo.get("firstName").asText());
            nguoiDat.setTen(contactInfo.get("lastName").asText());
            nguoiDat.setSoDienThoai(contactInfo.get("phone").asText());
            nguoiDat.setEmail(email);
            nguoiDat.setProvider("GOOGLE");
            nguoiDat.setEnable(true);
            Role userRole = roleRepostiory.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role ROLE_USER not found"));
            nguoiDat.setRoles(Set.of(userRole));
            return nguoiDatRepository.save(nguoiDat);
        }
    }

    private ChuyenBay luuChuyenBay(JsonNode flightInfo) throws Exception {
        String sanBayDi = flightInfo.get("departureAirport").asText();
        String sanBayDen = flightInfo.get("arrivalAirport").asText();
        Date ngayGioDi = sdfTime.parse(flightInfo.get("departureAt").asText());
        Date ngayGioDen = sdfTime.parse(flightInfo.get("arrivalAt").asText());
        String hangBay = flightInfo.get("airlineBrand").asText();

        Optional<ChuyenBay> existingChuyenBay = chuyenBayRepository.findBySanBayDiAndSanBayDenAndNgayGioDiAndNgayGioDenAndHangBay(
                sanBayDi, sanBayDen, ngayGioDi, ngayGioDen, hangBay);

        if (existingChuyenBay.isPresent()) {
            return existingChuyenBay.get();
        }

        ChuyenBay chuyenBay = new ChuyenBay();
        chuyenBay.setSanBayDi(sanBayDi);
        chuyenBay.setSanBayDen(sanBayDen);
        chuyenBay.setNgayGioDi(ngayGioDi);
        chuyenBay.setNgayGioDen(ngayGioDen);
        chuyenBay.setHangBay(hangBay);

        return chuyenBayRepository.save(chuyenBay);
    }


    private ThongTinDatVe luuThongTinDatVe(NguoiDat nguoiDat, double totalPrice) {
        ThongTinDatVe thongTinDatVe = new ThongTinDatVe();
        thongTinDatVe.setIdNguoiDat(nguoiDat);
        thongTinDatVe.setLoaiThanhToan("vnpay");
        thongTinDatVe.setTrangThaiThanhToan("Chờ thanh toán");
        thongTinDatVe.setTongTien(BigDecimal.valueOf(totalPrice));
        return thongTinDatVeRepository.save(thongTinDatVe);
    }

    private PhieuDatVe taoPhieuDatVe(
            JsonNode passenger,
            ThongTinDatVe thongTinDatVe,
            ChuyenBay chuyenBay,
            String classType) throws Exception {
        String type =passenger.get("type").asText();
        String typeFormat ="";
        if( type.equals("ADULT")){
            typeFormat = "Người lớn";
        }
        else if (type.equals("CHILDREN")) {
            typeFormat = "Trẻ em";
        }
        else{
            typeFormat = "Em bé";
        }
        String soHoChieu = passenger.get("passport").asText();
        KhachHang khachHang = luuKhachHang(passenger,soHoChieu,typeFormat);

        SecureRandom random = new SecureRandom();
        String maVe = String.format("%08d", random.nextInt(10000000));

        String classTypeFormat = "";
        if( classType.equals("STANDARD")){
            classTypeFormat = "Phổ thông";
        } else if (classType.equals("BUSINESS")) {
            classTypeFormat = "Thương gia";
        }
        else {//FIRST
            classTypeFormat = "Hạng nhất";
        }

        PhieuDatVe phieuDatVe = new PhieuDatVe();
        phieuDatVe.setMaVe(maVe);
        phieuDatVe.setIdThongTinDatVe(thongTinDatVe);
        phieuDatVe.setIdKhachHang(khachHang);
        phieuDatVe.setIdChuyenBay(chuyenBay);
        phieuDatVe.setNgayDat(LocalDate.now());
        phieuDatVe.setHangVe(classTypeFormat);
        phieuDatVe.setGiaVe(new BigDecimal(passenger.get("priceTicket").asText()));

        return phieuDatVe;
    }

    private void luuDanhSachPhieuDatVe(JsonNode passengers, ThongTinDatVe thongTinDatVe, ChuyenBay chuyenBay,String classType) throws Exception {
        if (passengers.isArray()) {
            for (JsonNode passenger : passengers) {
                PhieuDatVe phieuDatVe = taoPhieuDatVe(passenger, thongTinDatVe, chuyenBay,classType);
                phieuDatVeRepository.save(phieuDatVe);
            }
        }
    }

    private KhachHang luuKhachHang(JsonNode passenger, String soHoChieu, String typeFormat) throws ParseException {
        Optional<KhachHang> existingSoHoChieu = khachHangRepository.findBySoHoChieu(soHoChieu);
        if (existingSoHoChieu.isPresent()) {
            return existingSoHoChieu.get();
        }
        else{
            KhachHang khachHang = new KhachHang();
            khachHang.setHo(passenger.get("firstName").asText());
            khachHang.setTen(passenger.get("lastName").asText());
            khachHang.setNgaySinh(sdf.parse(passenger.get("birthDate").asText()));
            khachHang.setDanhXung(passenger.get("title").asText());
            khachHang.setLoaiKhachHang(typeFormat);
            JsonNode passport = passenger.get("passport");
            khachHang.setSoHoChieu(passport.get("number").asText());
            khachHang.setNgayHetHan(sdf.parse(passport.get("expirationDate").asText()));
            khachHang.setQuocGiaCap(passport.get("issuingCountry").asText());

            khachHangRepository.save(khachHang);
            return khachHang;
        }
    }

    public NguoiDat timThongTinNguoiDatTheoEmail(String email) {
        NguoiDat nguoiDat = nguoiDatRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người đặt với email: " + email));
        return nguoiDat;
    }

    public boolean kiemTraLoaiTaiKhoan(String email) {
        NguoiDat user = nguoiDatRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy email"));

        // Tài khoản đăng nhập Google và chưa có password => không được đổi mật khẩu
        return !(user.getProvider().equalsIgnoreCase("GOOGLE") && (user.getPassword() == null || user.getPassword().isEmpty()));
    }

    public boolean changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        Optional<NguoiDat> userOptional = nguoiDatRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            NguoiDat user = userOptional.get();

            // Kiểm tra mật khẩu cũ
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {

                // Kiểm tra mật khẩu mới và xác nhận mật khẩu mới
                if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                    // Cập nhật mật khẩu mới
                    user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                    nguoiDatRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }
}
