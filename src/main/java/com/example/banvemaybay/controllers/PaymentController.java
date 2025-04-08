package com.example.banvemaybay.controllers;

import com.example.banvemaybay.dtos.PaymentDTO;
import com.example.banvemaybay.services.EmailService;
import com.example.banvemaybay.services.PaymentService;
import com.example.banvemaybay.services.ThongTinDatVeService;
import com.example.banvemaybay.responses.ResponseObject;
import com.example.banvemaybay.utils.NumberEncryptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix:/api/v1}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final ThongTinDatVeService thongTinDatVeService;
    private final EmailService emailService;

    @GetMapping("/vn-pay")
    public ResponseEntity<?> vnPay(
            HttpServletRequest request,
            @RequestParam("amount") Long amount,
            @RequestParam("id") int id) {
        try {
            request.setAttribute("amount", amount);
            request.setAttribute("id", id);

            PaymentDTO.VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request);

            if (vnPayResponse != null && vnPayResponse.paymentUrl != null) {
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(vnPayResponse.paymentUrl)).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Không thể tạo URL thanh toán, vui lòng thử lại sau.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi trong quá trình xử lý thanh toán: " + e.getMessage());
        }
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<Void> payCallbackHandler(HttpServletRequest request) throws Exception {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_TxnRef");
        int decodedId = NumberEncryptor.decryptId(orderId);
        String redirectUrl = "";

        if ("00".equals(status)) {
            thongTinDatVeService.updateTrangThaiThanhToan(decodedId, "Thành công");
            emailService.sendBookingConfirmationEmail(decodedId);
            redirectUrl = "http://localhost:5173/payment-result?status=success";
        } else if ("24".equals(status)) {

            thongTinDatVeService.updateTrangThaiThanhToan(decodedId, "Thất bại");
            redirectUrl = "http://localhost:5173/payment-result?status=failure"; // Nếu thất bại, chuyển hướng kèm trạng thái
        }

        return ResponseEntity.status(HttpStatus.FOUND) // Mã 302 (redirect)
                .location(URI.create(redirectUrl)) // Chuyển hướng trực tiếp
                .build();
    }

//    @GetMapping("/momo/create-payment")
//    public String createPayment(@RequestParam long amount, @RequestParam String orderId) {
//        try {
//            return momoService.createPayment(amount, orderId);
//        } catch (Exception e) {
//            return "Lỗi thanh toán MoMo: " + e.getMessage();
//        }
//    }
//
//    @PostMapping("/momo/payment-notify")
//    public ResponseEntity<String> paymentNotify(@RequestBody String requestData) {
//        // Xử lý thông báo từ MoMo (parsing và kiểm tra dữ liệu)
//        System.out.println("Received MoMo notification: " + requestData);
//
//        // Thực hiện các bước xử lý sau khi nhận thông báo (cập nhật trạng thái đơn hàng...)
//
//        return ResponseEntity.ok("Success");
//    }
//
//    @GetMapping("/momo/payment-return")
//    public ResponseEntity<String> paymentReturn(@RequestParam String orderId, @RequestParam String status) {
//        // Xử lý trạng thái thanh toán sau khi người dùng được điều hướng về returnUrl
//        if ("success".equals(status)) {
//            // Xử lý khi thanh toán thành công
//            System.out.println("Payment successful for orderId: " + orderId);
//        } else {
//            // Xử lý khi thanh toán thất bại
//            System.out.println("Payment failed for orderId: " + orderId);
//        }
//
//        // Trả về kết quả hoặc hiển thị trang thông báo cho người dùng
//        return ResponseEntity.ok("Payment result: " + status);
//    }
}
