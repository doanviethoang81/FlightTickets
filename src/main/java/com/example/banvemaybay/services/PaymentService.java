package com.example.banvemaybay.services;

import com.example.banvemaybay.configs.VNPAYConfig;
import com.example.banvemaybay.dtos.PaymentDTO;
import com.example.banvemaybay.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VNPAYConfig vnPayConfig;

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Long.parseLong(request.getParameter("amount"));
        int orderId = Integer.parseInt(request.getParameter("id"));
//        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = "NCB"; // Hoặc lấy từ request nếu cần
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_TxnRef", String.valueOf(orderId));
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        queryUrl += "&vnp_SecureHash=" + VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

}
