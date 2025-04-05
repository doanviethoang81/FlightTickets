package com.example.banvemaybay.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public abstract class PaymentDTO {

    @AllArgsConstructor
    @Getter
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
