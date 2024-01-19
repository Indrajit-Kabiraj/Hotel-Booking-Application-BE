package com.example.hotelservice.DTOs;

import com.example.hotelservice.Models.Hotels;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class HotelResponseDTO {
    Long hotelCount;
    List<Hotels> hotels;

    public Long getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(Long hotelCount) {
        this.hotelCount = hotelCount;
    }

    public List<Hotels> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotels> hotels) {
        this.hotels = hotels;
    }
}
