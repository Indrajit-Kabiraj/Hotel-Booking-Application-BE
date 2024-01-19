package com.example.hotelservice.DTOs;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class RoomSearchDTO {
    private Long hotelId;
    private LocalDate startTime;

    private LocalDate endTime;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
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

    @Override
    public String toString() {
        return "RoomSearchDTO{" +
                "hotelId=" + hotelId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
