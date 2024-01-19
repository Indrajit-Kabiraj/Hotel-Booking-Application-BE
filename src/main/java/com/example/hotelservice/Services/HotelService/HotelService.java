package com.example.hotelservice.Services.HotelService;

import com.example.hotelservice.Models.Hotels;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public interface HotelService {
    Hotels getHotelByCountryAndState(String country, String state);
    CompletionStage<Hotels> getHotel(Long hotelId, String country);
    Hotels addHotel(Hotels hotel);
    Object updateHotel(Hotels hotel, Long id);
    Object deleteHotel(Long id);
}
