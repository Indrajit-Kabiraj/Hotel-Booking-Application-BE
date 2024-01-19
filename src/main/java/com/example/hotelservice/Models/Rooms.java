package com.example.hotelservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
public class Rooms {
    @Id
    @SequenceGenerator(name="idSequenceRooms", sequenceName="ID_SEQUENCE_ROOMS", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSequenceRooms")
    private Long room_category_id;
    @NotNull
    private String room_category_name;
    @NotNull
    private float room_price;
    private String room_status;
    @NotNull
    private Long num_of_person;
    @NotNull
    private Long num_of_rooms;
    private Long hotel_id;

    public Rooms(){

    }

    public Rooms(String room_category_name, float room_price, String room_status, Long num_of_rooms, Long num_of_person, Long hotel_id) {
        this.room_category_name = room_category_name;
        this.room_price = room_price;
        this.room_status = room_status;
        this.num_of_rooms = num_of_rooms;
        this.num_of_person = num_of_person;
        this.hotel_id = hotel_id;
    }

    public Long getroom_category_id() {
        return room_category_id;
    }

    public void setroom_category_id(Long room_category_id) {
        this.room_category_id = room_category_id;
    }

    public String getroom_category_name() {
        return room_category_name;
    }

    public void setroom_category_name(String room_category_name) {
        this.room_category_name = room_category_name;
    }

    public Long getNum_of_rooms() {
        return num_of_rooms;
    }

    public void setNum_of_rooms(Long num_of_rooms) {
        this.num_of_rooms = num_of_rooms;
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

    public Long getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Long hotel_id) {
        this.hotel_id = hotel_id;
    }


    @Override
    public String toString() {
        return "Rooms{" +
                "room_category_id=" + room_category_id +
                ", room_category_name='" + room_category_name + '\'' +
                ", room_price=" + room_price +
                ", room_status='" + room_status + '\'' +
                ", num_of_person=" + num_of_person +
                ", num_of_rooms=" + num_of_rooms +
                ", hotel_id=" + hotel_id +
                '}';
    }
}
