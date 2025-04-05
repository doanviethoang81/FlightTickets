package com.example.banvemaybay.services;

import com.example.banvemaybay.configs.MoMoConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MomoService {
    private final MoMoConfig momoConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String createPayment(long amount, String orderId) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toán MoMo đơn hàng " + orderId;
        String extraData = "";
        String requestType = "payWithMethod";

        // Tạo rawData để ký HMAC SHA256
        String rawData =
                "accessKey=" + momoConfig.getAccessKey() +
                        "&amount=" + amount +
                        "&extraData=" + extraData +
                        "&ipnUrl=" + momoConfig.getNotifyUrl() +
                        "&orderId=" + orderId +
                        "&orderInfo=" + orderInfo +
                        "&partnerCode=" + momoConfig.getPartnerCode() +
                        "&redirectUrl=" + momoConfig.getReturnUrl() +
                        "&requestId=" + requestId +
                        "&requestType=" + requestType;

        // Tạo chữ ký HMAC SHA256
        String signature = hmacSHA256(momoConfig.getSecretKey(), rawData);


        // Tạo request JSON
        String requestBody = "{"
                + "\"partnerCode\":\"" + momoConfig.getPartnerCode() + "\","
                + "\"accessKey\":\"" + momoConfig.getAccessKey() + "\","
                + "\"requestId\":\"" + requestId + "\","
                + "\"amount\":\"" + amount + "\","
                + "\"orderId\":\"" + orderId + "\","
                + "\"orderInfo\":\"" + orderInfo + "\","
                + "\"redirectUrl\":\"" + momoConfig.getReturnUrl() + "\","
                + "\"ipnUrl\":\"" + momoConfig.getNotifyUrl() + "\","
                + "\"lang\":\"" + "vi" + "\","
                + "\"extraData\":\"" + extraData + "\","
                + "\"requestType\":\"" + requestType + "\","
                + "\"signature\":\"" + signature + "\""
                + "}";

        System.out.println("Response from MoMo: " + requestBody);

        // Gửi request đến MoMo API
        String response = restTemplate.postForObject(momoConfig.getEndpoint(), requestBody, String.class);

        // Trích xuất URL thanh toán từ response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("payUrl").asText();
    }

    public static String hmacSHA256(String secretKey, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
