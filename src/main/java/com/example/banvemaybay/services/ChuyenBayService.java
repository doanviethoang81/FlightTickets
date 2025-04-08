package com.example.banvemaybay.services;

import com.example.banvemaybay.models.ChuyenBay;
import com.example.banvemaybay.repositorys.ChuyenBayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChuyenBayService implements IChuyenBayService{

    private final ChuyenBayRepository chuyenBayRepository;
    @Override
    public ChuyenBay save(ChuyenBay chuyenBay) {
        return chuyenBayRepository.save(chuyenBay);
    }

    @Override
    public List<ChuyenBay> getAllChuyenBay() {
        return chuyenBayRepository.findAll();
    }
}
