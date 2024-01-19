package com.example.hotelservice.Controllers;

import com.example.hotelservice.DTOs.HotelResponseDTO;
import com.example.hotelservice.DTOs.HotelSearchDTO;
import com.example.hotelservice.DTOs.RoomSearchDTO;
import com.example.hotelservice.DTOs.UserHotelRatingDTO;
import com.example.hotelservice.Exceptions.HotelsNotFoundException;
import com.example.hotelservice.Models.Hotels;
import com.example.hotelservice.Models.Rooms;
import com.example.hotelservice.Services.HotelService.HotelServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hotels/")
public class HotelController {


    @Autowired
    private final HotelServiceImpl hotelService;


    @GetMapping("/_search")
    @Async
    public CompletionStage<ResponseEntity<HotelResponseDTO>> getHotelByCountryAndState(@RequestParam(name = "country", required = true) String country,
                                                                                         @RequestParam(name = "state", required = true) String state){
        return hotelService.getHotelByCountryAndState(country, state).thenApplyAsync(res->new ResponseEntity<HotelResponseDTO>(res, HttpStatus.OK));
    }



    @PostMapping("/getAvailableHotels")
    @Async
    public CompletionStage<ResponseEntity<Object>> getAllAvailableHotels(@RequestBody HotelSearchDTO searchHotels) throws HotelsNotFoundException{

        try {
            return hotelService.getAvailableHotelsByCountryAndState(searchHotels).thenApplyAsync(res->new ResponseEntity<Object>(res, HttpStatus.OK));
        } catch (HotelsNotFoundException e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @GetMapping("/_search/{hotelId}")
    @Async
    public ResponseEntity<Object> getHotel(@PathVariable Long hotelId, @RequestParam(name = "country", required = false) String country) throws Exception {
        try {
            Hotels hotels =  hotelService.getHotel(hotelId,country);
            return new ResponseEntity<Object>(hotels, HttpStatus.OK);
        } catch ( HotelsNotFoundException e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
            return objectResponseEntity;
        }
    }

    @PostMapping("/create")
    @Async
    public CompletableFuture<ResponseEntity<Map<String,Hotels>>> addHotel(@RequestBody Hotels hotel){
        return CompletableFuture.completedFuture(new ResponseEntity<Map<String,Hotels>>(hotelService.addHotel(hotel), HttpStatus.OK));
    }

    @PatchMapping("/update/{id}")
    @Async
    public CompletableFuture<ResponseEntity<Object>> updateHotel(@RequestBody Hotels hotel, @PathVariable Long id){
        try{
            return CompletableFuture.completedFuture(new ResponseEntity<>(hotelService.updateHotel(hotel, id), HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Async
    public CompletableFuture<ResponseEntity<Object>> deleteHotel(@PathVariable Long id){
        try{
            return CompletableFuture.completedFuture(new ResponseEntity<>(hotelService.deleteHotel(id), HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @PostMapping("/updateRating/")
    @RabbitListener(queues = {"q.hotel-review"})
    public CompletionStage<ResponseEntity<Map<String, String>>> updateRating(@RequestBody String reviewMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(reviewMessage);
        UserHotelRatingDTO review = mapper.readValue(reviewMessage, UserHotelRatingDTO.class);
        Map<String,String> ratingResp = hotelService.updateRatingForAHotel(review);
        return CompletableFuture.completedFuture(new ResponseEntity<Map<String, String>>(ratingResp, HttpStatus.OK));
    }
}
