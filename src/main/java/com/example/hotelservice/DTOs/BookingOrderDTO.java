package com.example.hotelservice.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class BookingOrderDTO {
    private Long orderHotelId;

    private Long orderRoomCategoryId;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate orderDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate orderStartTime;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate orderEndTime;

    private String orderStatus;

    private Long orderDuration;

    private List<UserDTO> users;

    private Long parentUserId;

    private String bookingRefId;

    private Long maxRoomNumber;

    private float roomPrice;

    public float getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(float roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Long getMaxRoomNumber() {
        return maxRoomNumber;
    }

    public void setMaxRoomNumber(Long maxRoomNumber) {
        this.maxRoomNumber = maxRoomNumber;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUser) {
        this.parentUserId = parentUser;
    }

    public Long getOrderHotelId() {
        return orderHotelId;
    }

    public void setOrderHotelId(Long orderHotelId) {
        this.orderHotelId = orderHotelId;
    }

    public Long getOrderRoomCategoryId() {
        return orderRoomCategoryId;
    }

    public void setOrderRoomCategoryId(Long orderRoomCategoryId) {
        this.orderRoomCategoryId = orderRoomCategoryId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(LocalDate orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public LocalDate getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(LocalDate orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderDuration() {
        return orderDuration;
    }

    public void setOrderDuration(Long orderDuration) {
        this.orderDuration = orderDuration;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
