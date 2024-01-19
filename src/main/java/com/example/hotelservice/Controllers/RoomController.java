package com.example.hotelservice.Controllers;

import com.example.hotelservice.DTOs.BookingOrderDTO;
import com.example.hotelservice.DTOs.RoomCreationDTO;
import com.example.hotelservice.DTOs.RoomSearchDTO;
import com.example.hotelservice.Exceptions.HotelsNotFoundException;
import com.example.hotelservice.Models.Rooms;
import com.example.hotelservice.Services.RoomService.RoomServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping(value = "/v1/rooms", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CacheConfig(cacheNames = "roomsCache")
public class RoomController {

    private final RoomServiceImpl roomService;

    @GetMapping("/_search")
    @Async
    public CompletionStage<ResponseEntity<Object>> getRooms(@RequestParam(name = "hotelId", required = true) Long hotelId
                                                                 ){
        return roomService.getRoomsForAHotelPerCategory(hotelId).thenApplyAsync(res->new ResponseEntity<Object>(res,HttpStatus.OK));
    }

    @GetMapping("/_search/{id}")
    @Async
    public CompletionStage<ResponseEntity<Object>> getRoom(@PathVariable(name = "id") Long roomCategoryId,
                                                            @RequestParam(name="category", required = false) String category) {
        try{
            return roomService.getRoom(roomCategoryId, category).thenApplyAsync(res -> new ResponseEntity<Object>(res,HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @PostMapping("/_create")
    @Async
    public CompletionStage<ResponseEntity<Object>> addRoom(@RequestBody RoomCreationDTO room){
        try{
            return roomService.createRoom(room).thenApplyAsync(res->new ResponseEntity<Object>(res,HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @PatchMapping("/update/{roomCategoryId}")
    @Async
    public CompletionStage<ResponseEntity<Object>> updateRoom(@RequestBody RoomCreationDTO room, @PathVariable Long roomCategoryId){
        try{
            return roomService.updateRoom(room,roomCategoryId).thenApplyAsync(res->new ResponseEntity<Object>(res,HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @DeleteMapping("/delete/{roomCategoryId}")
//    @CacheEvict(key = "#id",beforeInvocation = true)
    public CompletionStage<ResponseEntity<Object>> deleteRoom(@PathVariable Long roomCategoryId){
        try{
            return roomService.deleteRoom(roomCategoryId).thenApplyAsync(res->new ResponseEntity<Object>(res,HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @PostMapping("/getAvailableRooms")
    @Async
    public CompletionStage<ResponseEntity<Object>> getAvailableRooms(@RequestBody RoomSearchDTO roomSearchQuery) throws HotelsNotFoundException {
        try{
            return roomService.getAvailableRoomsByHotelId(roomSearchQuery).thenApplyAsync(res->new ResponseEntity<Object>(res, HttpStatus.OK));
        } catch (HotelsNotFoundException e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.NO_CONTENT);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }

    }

    @PostMapping("/booking/{roomCategoryId}")
    @Async
    public CompletionStage<ResponseEntity<Object>> createOrder(@PathVariable Long roomCategoryId, @RequestBody BookingOrderDTO createBookingData) throws JsonProcessingException {
        return roomService.createOrderForRoom(roomCategoryId ,createBookingData).thenApplyAsync(res->new ResponseEntity<Object>(res, HttpStatus.OK));
    }

    @GetMapping("/getInfo")
    public ResponseEntity<Map<String, Map<String ,String>>> getHotelAndRoomInfo(@RequestParam Long hotelId, @RequestParam Long roomCategoryId) throws Exception {
        return new ResponseEntity<Map<String, Map<String ,String>>>(roomService.getRoomAndHotelInfo(hotelId, roomCategoryId), HttpStatus.OK);
    }
}
