package com.example.hotelservice.DTOs;
;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class HotelSearchDTO {
    private String country;
    private String state;

    private LocalDate startTime;

    private LocalDate endTime;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

}
