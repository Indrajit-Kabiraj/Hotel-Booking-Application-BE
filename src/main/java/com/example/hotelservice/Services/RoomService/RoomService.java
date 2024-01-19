package com.example.hotelservice.Services.RoomService;

import com.example.hotelservice.Models.Rooms;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public interface RoomService {

    CompletionStage<Map<String,Object>> getRoomsForAHotelPerCategory(Long hotelId);

    CompletionStage<Rooms> getRoom(String roomCategoryId, String category);

    CompletionStage<ResponseEntity<Rooms>> createRoom(Rooms room);

    CompletionStage<Object> updateRoom(Rooms room, Long roomCategoryId);

    CompletionStage<Object> deleteRoom(Long roomCategoryId);
}
