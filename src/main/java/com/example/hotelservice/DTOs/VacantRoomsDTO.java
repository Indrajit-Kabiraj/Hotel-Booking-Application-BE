package com.example.hotelservice.DTOs;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class VacantRoomsDTO{

    Long hotelId;
    Long roomCategoryId;
    Long numberOfBookedRooms;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomCategoryId() {
        return roomCategoryId;
    }

    public void setRoomCategoryId(Long roomCategoryId) {
        this.roomCategoryId = roomCategoryId;
    }

    public Long getNumberOfBookedRooms() {
        return numberOfBookedRooms;
    }

    public void setNumberOfBookedRooms(Long numberOfVacantRooms) {
        this.numberOfBookedRooms = numberOfVacantRooms;
    }

    @Override
    public String toString() {
        return "VacantHotelsDTO{" +
                "hotelId=" + hotelId +
                ", roomCategoryId=" + roomCategoryId +
                ", numberOfVacantRooms=" + numberOfBookedRooms +
                '}';
    }
}

