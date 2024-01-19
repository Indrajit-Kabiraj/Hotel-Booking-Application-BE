package com.example.hotelservice.Services.AmmenityService;

import com.example.hotelservice.Models.Ammenities;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public interface AmmenityService {
    CompletionStage<List<Ammenities>> getAmmenities();

    CompletionStage<Object> createAmmenities(Ammenities ammenities);

    CompletionStage<Object> updateAmmenity(Long id, String ammenityName);
}
