package com.example.hotelservice.DTOs;

import java.util.List;

public class RoomsResponseDTO {
    private Long room_category_id;
    private String room_category_name;
    private float room_price;
    private String room_status;
    private Long num_of_person;
    private Long num_of_rooms;
    private Long hotel_id;
    private List<String> ammenities;

    public RoomsResponseDTO(Long room_category_id, String room_category_name, float room_price, String room_status, Long num_of_person, Long num_of_rooms, Long hotel_id, List<String> ammenities) {
        this.room_category_id = room_category_id;
        this.room_category_name = room_category_name;
        this.room_price = room_price;
        this.room_status = room_status;
        this.num_of_person = num_of_person;
        this.num_of_rooms = num_of_rooms;
        this.hotel_id = hotel_id;
        this.ammenities = ammenities;
    }

    public Long getRoom_category_id() {
        return room_category_id;
    }

    public void setRoom_category_id(Long room_category_id) {
        this.room_category_id = room_category_id;
    }

    public String getRoom_category_name() {
        return room_category_name;
    }

    public void setRoom_category_name(String room_category_name) {
        this.room_category_name = room_category_name;
    }

    public float getRoom_price() {
        return room_price;
    }

    public void setRoom_price(float room_price) {
        this.room_price = room_price;
    }

    public String getRoom_status() {
        return room_status;
    }

    public void setRoom_status(String room_status) {
        this.room_status = room_status;
    }

    public Long getNum_of_person() {
        return num_of_person;
    }

    public void setNum_of_person(Long num_of_person) {
        this.num_of_person = num_of_person;
    }

    public Long getNum_of_rooms() {
        return num_of_rooms;
    }

    public void setNum_of_rooms(Long num_of_rooms) {
        this.num_of_rooms = num_of_rooms;
    }

    public Long getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Long hotel_id) {
        this.hotel_id = hotel_id;
    }

    public List<String> getAmmenities() {
        return ammenities;
    }

    public void setAmmenities(List<String> ammenities) {
        this.ammenities = ammenities;
    }
}
