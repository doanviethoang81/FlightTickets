package com.example.banvemaybay.services;

import com.example.banvemaybay.models.ChuyenBay;

import java.util.List;

public interface IChuyenBayService{
    ChuyenBay save(ChuyenBay chuyenBay);
    List<ChuyenBay> getAllChuyenBay();
}
