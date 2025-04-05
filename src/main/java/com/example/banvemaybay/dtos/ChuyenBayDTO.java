package com.example.banvemaybay.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ChuyenBayDTO {

    @JsonProperty("departureAirport")
    private String sanBayDi;

    @JsonProperty("arrivalAirport")
    private String sanBayDen;

    @JsonProperty("departureAt")
    private Date ngayGioDi;

    @JsonProperty("arrivalAt")
    private Date ngayGioDen;

    @JsonProperty("airlineBrand")
    private String hangBay;

}
