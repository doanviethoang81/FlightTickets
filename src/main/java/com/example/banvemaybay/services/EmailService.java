package com.example.banvemaybay.services;

import com.example.banvemaybay.components.ActivationCodeCache;
import com.example.banvemaybay.models.NguoiDat;
import com.example.banvemaybay.models.PhieuDatVe;
import com.example.banvemaybay.models.ThongTinDatVe;
import com.example.banvemaybay.repositorys.NguoiDatRepository;
import com.example.banvemaybay.repositorys.PhieuDatVeRepository;
import com.example.banvemaybay.repositorys.ThongTinDatVeRepository;
import com.example.banvemaybay.utils.NumberEncryptor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final ThongTinDatVeRepository thongTinDatVeRepository;
    private final PhieuDatVeRepository phieuDatVeRepository;
    private final NguoiDatRepository nguoiDatRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    private ActivationCodeCache activationCodeCache;

    @Async
    public void sendBookingConfirmationEmail(Integer idThongTinDatVe) throws Exception {
        // Lấy thông tin đặt vé
        ThongTinDatVe thongTinDatVe = new ThongTinDatVe();
        Optional<ThongTinDatVe> existingThongTinDatVe = thongTinDatVeRepository.findById(idThongTinDatVe);
        if(existingThongTinDatVe.isPresent()) {
            thongTinDatVe = existingThongTinDatVe.get();
        }
        else{
            System.out.println("Không có thông tin vé");
            return;
        }
        // Lấy danh sách vé của đơn đặt
        List<PhieuDatVe> danhSachVe = phieuDatVeRepository.findByIdThongTinDatVe(thongTinDatVe);

        // Tạo dữ liệu cho template
        org.thymeleaf.context.Context context = new Context();
        context.setVariable("thongTinDatVe", thongTinDatVe);
        context.setVariable("danhSachVe", danhSachVe);

        // Render template email
        String htmlContent = templateEngine.process("email-template", context);
        //mã hóa lại id đơn hàng
        String encryptedId = NumberEncryptor.encryptId(thongTinDatVe.getId());
        // Gửi email
        sendEmail(thongTinDatVe.getIdNguoiDat().getEmail(), "✈\uFE0F Xác nhận đặt vé thành công – [Mã đơn hàng: #"+ encryptedId+"]", htmlContent);
    }

    private void sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi email", e);
        }
    }

    public  String generateActivationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Tạo số ngẫu nhiên từ 100000 đến 999999
        return String.valueOf(code);
    }

    // gửi email xác thực taì khoản
    @Async
    public void sendActivationEmail(String email, String activationCode) {
        try {
            // Tạo MimeMessage và Helper
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Dữ liệu truyền vào template Thymeleaf
            Context context = new Context();
            context.setVariable("userName", email); // Hoặc thay bằng tên thật nếu có
            context.setVariable("activationCode", activationCode);

            // Render HTML từ template
            String htmlContent = templateEngine.process("verify-registration-email", context);

            // Thiết lập thông tin email
            helper.setTo(email);
            helper.setSubject("✈\uFE0F Mã kích hoạt tài khoản");
            helper.setText(htmlContent, true); // Gửi nội dung HTML

            // Gửi email
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //gửi email xác thực cập nhật password
    @Async
    public void sendPasswordUpdateEmail(String email, String activationCode) {
        try {
            // Tạo MimeMessage và Helper
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Dữ liệu truyền vào template Thymeleaf
            Context context = new Context();
            context.setVariable("userName", email); // Hoặc thay bằng tên thật nếu có
            context.setVariable("activationCode", activationCode);

            // Render HTML từ template
            String htmlContent = templateEngine.process("email-verification-update-password", context);

            // Thiết lập thông tin email
            helper.setTo(email);
            helper.setSubject("✈\uFE0F Mã xác nhận tài khoản tạo mật khẩu ");
            helper.setText(htmlContent, true); // Gửi nội dung HTML

            // Gửi email
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyActivationCode(String email, String activationCode) {
        Optional<NguoiDat> optionalUser = nguoiDatRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            NguoiDat user = optionalUser.get();

            // Kiểm tra mã kích hoạt và thời gian hết hạn
            if (user.getActivationCode().equals(activationCode) && LocalDateTime.now().isBefore(user.getActivationCodeExpiry())) {
                user.setEnable(true); // Kích hoạt tài khoản
                user.setActivationCode(null);  // Xóa mã kích hoạt sau khi xác thực
                nguoiDatRepository.save(user);  // Lưu lại đối tượng đã được cập nhật
                return true;
            }
        }
        return false;
    }

    public boolean verifyActivationCodeUser(String email, String activationCode) {
        // Kiểm tra mã đã hết hạn chưa
        if (activationCodeCache.isCodeExpired(email)) {
            return false;  // Mã không hợp lệ
        }
        // Lấy mã xác thực đã lưu trong bộ nhớ
        ActivationCodeCache.ActivationCodeInfo codeInfo = activationCodeCache.getCode(email);
        if (codeInfo != null && codeInfo.getCode().equals(activationCode)) {
            // Nếu mã nhập vào khớp với mã lưu trong bộ nhớ
            return true;  // Mã hợp lệ
        }
        // Nếu mã không khớp
        return false;  // Mã không hợp lệ
    }
}
