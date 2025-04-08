package com.example.banvemaybay.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"id", "ho","ten", "soDienThoai", "email", "enable","provider"})
public class UserRoleAdminDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("ho")
    private String ho;

    @JsonProperty("ten")
    private String ten;

    @JsonProperty("soDienThoai")
    private String soDienThoai;

    @JsonProperty("email")
    private String email;

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("provider")
    private String provider;
}
