package com.example.hotelservice.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "room_ammenities")
public class RoomAmmenities {

    @Id
    @SequenceGenerator(name="idSequenceRoomAmmenities", sequenceName="ID_SEQUENCE_ROOM_AMMENITUES", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSequenceRoomAmmenities")
    private Long id;

    @Column(name="room_category_id")
    private Long roomCategoryId;

    @Column(name="ammenity_id")
    private Long ammenityId;

    @Column(name="ammenity_name")
    private String ammenityName;


    public RoomAmmenities(){

    }

    public RoomAmmenities(Long roomCategoryId, Long ammenityId, String ammenityName) {
        this.roomCategoryId = roomCategoryId;
        this.ammenityId = ammenityId;
        this.ammenityName = ammenityName;
    }

    public Long getRoomCategoryId() {
        return roomCategoryId;
    }

    public void setRoomCategoryId(Long roomCategoryId) {
        this.roomCategoryId = roomCategoryId;
    }

    public Long getAmmenityId() {
        return ammenityId;
    }

    public void setAmmenityId(Long ammenityId) {
        this.ammenityId = ammenityId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAmmenityName() {
        return ammenityName;
    }

    public void setAmmenityName(String ammenityName) {
        this.ammenityName = ammenityName;
    }

    @Override
    public String toString() {
        return "RoomAmmenities{" +
                "id=" + id +
                ", roomCategoryId=" + roomCategoryId +
                ", ammenityId=" + ammenityId +
                '}';
    }
}
