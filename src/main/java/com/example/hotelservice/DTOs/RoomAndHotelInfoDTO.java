package com.example.hotelservice.DTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAndHotelInfoDTO {
    String hotelName;
    String hotelAddress;
    String hotelCountry;
    String hotelState;
    String roomName;

}
