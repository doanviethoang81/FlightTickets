package com.example.banvemaybay.configs;

import com.example.banvemaybay.utils.EnvUtil;
import com.example.banvemaybay.utils.VNPayUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Getter
@Configuration
public class VNPAYConfig {

    private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "https://flighttickets.onrender.com/api/v1/payment/vn-pay-callback";
    private final String vnp_TmnCode = EnvUtil.get("VNPAY_TMN_CODE");
    private final String secretKey = EnvUtil.get("VNPAY_HASH_SECRET");
    private final String vnp_Version = "2.1.0";
    private final String vnp_Command = "pay";
    private final String orderType = "other";

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);

        // ✅ Múi giờ chuẩn cho Việt Nam
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // ✅ Thời gian hiện tại + thời gian hết hạn
        ZonedDateTime currentTime = ZonedDateTime.now(vietnamZoneId);
        String vnpCreateDate = currentTime.format(formatter);
        String vnpExpireDate = currentTime.plusMinutes(15).format(formatter); // Hết hạn sau 15 phút

        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        vnpParamsMap.put("vnp_ExpireDate", vnpExpireDate);

        return vnpParamsMap;
    }


//    public Map<String, String> getVNPayConfig() {
//        Map<String, String> vnpParamsMap = new HashMap<>();
//        vnpParamsMap.put("vnp_Version", this.vnp_Version);
//        vnpParamsMap.put("vnp_Command", this.vnp_Command);
//        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
//        vnpParamsMap.put("vnp_CurrCode", "VND");
////        vnpParamsMap.put("vnp_TxnRef",  VNPayUtil.getRandomNumber(8));
//        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
//        vnpParamsMap.put("vnp_OrderType", this.orderType);
//        vnpParamsMap.put("vnp_Locale", "vn");
//        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
////        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
//        String vnpCreateDate = currentTime.format(formatter);
//        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
//        calendar.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = currentTime.plusMinutes(15).format(formatter);
////        String vnp_ExpireDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
//        return vnpParamsMap;
//    }
}
