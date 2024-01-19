package com.example.hotelservice.DTOs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHotelRatingDTO {
    float rating;
    Long hotelId;
    Long countOfRatings;

    public UserHotelRatingDTO(String reviewMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        UserHotelRatingDTO userReviewDTO = mapper.readValue(reviewMessage, UserHotelRatingDTO.class);
        this.hotelId = userReviewDTO.getHotelId();
        this.rating = userReviewDTO.getRating();
        this.countOfRatings = userReviewDTO.getCountOfRatings();
    }
}
