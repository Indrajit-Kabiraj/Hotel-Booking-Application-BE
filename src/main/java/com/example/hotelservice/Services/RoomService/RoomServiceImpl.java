package com.example.hotelservice.Services.RoomService;

import com.example.hotelservice.DTOs.*;
import com.example.hotelservice.Exceptions.HotelsNotFoundException;
import com.example.hotelservice.Models.Hotels;
import com.example.hotelservice.Models.Rooms;
import com.example.hotelservice.Repo.RoomsRepo;
import com.example.hotelservice.Services.HotelService.HotelService;
import com.example.hotelservice.Services.HotelService.HotelServiceImpl;
import com.example.hotelservice.Services.RoomAmmenityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl {

    @Autowired
    private final RoomsRepo roomsRepo;

    @Autowired
    private final HotelServiceImpl hotelService;
    @Autowired
    private final RoomAmmenityService roomAmmenityService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public CompletionStage<Map<String,Object>> getRoomsForAHotelPerCategory(Long hotelId){
        List<Rooms> roomsInAHotel = roomsRepo.findRoomsByHotelId(hotelId);
        List<RoomsResponseDTO> finalRooms = new ArrayList<>();
        roomsInAHotel.forEach(room->{
            List<String> roomAmmenities = roomAmmenityService.getAmmenitiesNameByRoomId(room.getroom_category_id());
            finalRooms.add(new RoomsResponseDTO(room.getroom_category_id(),
                    room.getroom_category_name(),
                    room.getRoom_price(),
                    room.getRoom_status(),
                    room.getNum_of_person(),
                    room.getNum_of_rooms(),
                    room.getHotel_id(),
                    roomAmmenities));
        });
        Optional<Rooms> min = roomsInAHotel.stream().min((x, y)-> Float.compare(x.getRoom_price(),y.getRoom_price()));
        Map<String,Object> res = new HashMap<>();
        res.put("roomsCount",roomsInAHotel.size());
        res.put("roomMinPrice",min.get().getRoom_price());
        res.put("rooms",finalRooms);
        return CompletableFuture.completedFuture(res);
    }

    public CompletionStage<Object> getRoom(Long roomCategoryId, String category){
        try{
            Optional<Rooms> room = roomsRepo.findRoomById(roomCategoryId);
            if(room.isPresent()){
                List<String> roomAmmenities = roomAmmenityService.getAmmenitiesNameByRoomId(roomCategoryId);
                RoomsResponseDTO result = new RoomsResponseDTO(room.get().getroom_category_id(),room.get().getroom_category_name(),
                        room.get().getRoom_price(),room.get().getRoom_status(),room.get().getNum_of_person(),room.get().getNum_of_rooms(),
                        room.get().getHotel_id(),roomAmmenities);
                return CompletableFuture.completedFuture(result);
            }
            else{
                throw new Exception();
            }
        } catch (Exception e){
            return CompletableFuture.completedFuture(e.getMessage());
        }
    }

    @Transactional
    public CompletionStage<RoomCreationDTO> createRoom(RoomCreationDTO room){
        Rooms newRoom = new Rooms(room.getRoom_category_name(), room.getRoom_price(), room.getRoom_status(), room.getNum_of_rooms(), room.getNum_of_person(), room.getHotel_id());
        newRoom.setRoom_status("ACTIVE");
        roomsRepo.save(newRoom);
        roomAmmenityService.createRoomAmmenities(newRoom.getroom_category_id(), room.getAmmenities());
        room.setRoom_category_id(newRoom.getroom_category_id());
        room.setRoom_status("ACTIVE");
        return CompletableFuture.completedFuture(room);
    }

    @Transactional
    public CompletionStage<Object> updateRoom(RoomCreationDTO room, Long roomCategoryId){
        try{
            Optional<Rooms> existingRoom = roomsRepo.findRoomById(roomCategoryId);
            if(existingRoom.isPresent()){
                if(room.getRoom_category_name()!=null) existingRoom.get().setroom_category_name(room.getRoom_category_name());
                if(room.getNum_of_rooms()!=null) existingRoom.get().setNum_of_rooms(room.getNum_of_rooms());
                if(room.getRoom_price()>0f) existingRoom.get().setRoom_price(room.getRoom_price());
                if(room.getRoom_status()!=null) existingRoom.get().setRoom_status(room.getRoom_status());
                if(room.getHotel_id() != null) existingRoom.get().setHotel_id(room.getHotel_id());
                if(room.getAmmenities() != null){
                    roomAmmenityService.updateRoomAmmenities(existingRoom.get().getroom_category_id(), room.getAmmenities());
                }
                roomsRepo.save(existingRoom.get());
                Map<String, Rooms> res= new HashMap<>();
                res.put("data", existingRoom.get());
                return CompletableFuture.completedFuture(res);
            }
            else{
                throw new Exception("Room Not Found!");
            }
        } catch (Exception e){
            return CompletableFuture.completedFuture(e.getMessage());
        }
    }

    @Transactional
    public CompletionStage<Object> deleteRoom(Long roomCategoryId){
        try{
            Optional<Rooms> existingRoom = roomsRepo.findRoomById(roomCategoryId);
            if(existingRoom.isPresent()){
                existingRoom.get().setRoom_status("ARCHIVED");
                roomsRepo.save(existingRoom.get());
                return CompletableFuture.completedFuture(existingRoom);
            }
            else{
                throw new Exception("Room Not Found!");
            }
        } catch (Exception e){
            return CompletableFuture.completedFuture(e.getMessage());
        }
    }

    public CompletionStage<List<Rooms>> getAvailableRoomsByHotelId(RoomSearchDTO searchRoomQuery) {
        List<Rooms> rooms = roomsRepo.findRoomsByHotelId(searchRoomQuery.getHotelId());
        List<VacantRoomsDTO> allHotelsByCountry = rooms.stream()
                .map(room -> new VacantRoomsDTO(
                        room.getHotel_id(),
                        room.getroom_category_id(),
                        room.getNum_of_rooms()
                ))
                .collect(Collectors.toList());
        List<Long> hotelIds = allHotelsByCountry.stream().map(hotel -> hotel.getHotelId()).distinct().collect(Collectors.toList());
        List<Long> roomCategoryIds = allHotelsByCountry.stream().map(hotel -> hotel.getRoomCategoryId()).collect(Collectors.toList());
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String URL = "http://localhost:8082/v1/bookings/_search/vacantHotels";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("hotelId",hotelIds);
        jsonObj.put("startTime", searchRoomQuery.getStartTime());
        jsonObj.put("endTime",searchRoomQuery.getEndTime());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObj.toString());
        List<VacantRoomsDTO> bookedRooms = new ArrayList<>();
        Request request = new Request.Builder().url(URL).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            String resp = response.body().string();
            JSONObject json = new JSONObject(resp);
            JSONArray respArray = json.getJSONArray("bookedRoons");
            for(int i=0;i<respArray.length();i++){
                VacantRoomsDTO val = new VacantRoomsDTO(Long.parseLong(respArray.getJSONObject(i).get("hotelId").toString()), Long.parseLong(respArray.getJSONObject(i).get("roomCategoryId").toString()), Long.parseLong(respArray.getJSONObject(i).get("numberOfVacantRooms").toString()));
                bookedRooms.add(val);
            }
            List<Long> availableRooms = new ArrayList<>(roomCategoryIds);
            /* TODO - Make this logic efficient */
            for(int i = 0; i < allHotelsByCountry.size(); i++){
                for( int j = 0 ; j < bookedRooms.size() ; j++){
                    if((bookedRooms.get(j).getRoomCategoryId() == allHotelsByCountry.get(i).getRoomCategoryId())){
                        if(bookedRooms.get(j).getNumberOfBookedRooms() >= allHotelsByCountry.get(i).getNumberOfBookedRooms()){
                            availableRooms.remove(allHotelsByCountry.get(i).getRoomCategoryId());
                        }
                    }
                }
            }
            if(availableRooms.size() == 0){
                throw new HotelsNotFoundException("No available rooms!");
            }
            return CompletableFuture.completedFuture(roomsRepo.findRoomsByIds(availableRooms));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletionStage<Object> createOrderForRoom(Long roomCategoryId, BookingOrderDTO createBookingData) throws JsonProcessingException {

        Map<String, Object> res = new HashMap<>();

        if(createBookingData.getParentUserId() == -1){
            res.put("order_status", "FAILED");
            res.put("error_message", "user_validation_failed");
            return CompletableFuture.completedFuture(res);
        }
        if(roomCategoryId != createBookingData.getOrderRoomCategoryId()){
            res.put("order_status", "FAILED");
            res.put("error_message", "room_id_is_incorrect");
            return CompletableFuture.completedFuture(res);
        }
        Date date = new Date();
        UUID refId = UUID.randomUUID();
        Optional<Rooms> room = roomsRepo.findRoomById(roomCategoryId);
        Long maxRooms = room.get().getNum_of_rooms();
        float amount = room.get().getRoom_price();
        System.out.println(roomsRepo.findRoomById(createBookingData.getOrderRoomCategoryId()).get());
        // Convert Date object to UTC LocalDate object
        Instant instant = date.toInstant();
        ZoneId utcZoneId = ZoneId.of("Z");
        ZonedDateTime zonedDateTime = instant.atZone(utcZoneId);
        LocalDate orderDate = zonedDateTime.toLocalDate();
        Period period = Period.between(createBookingData.getOrderStartTime(), createBookingData.getOrderEndTime());
        createBookingData.setOrderDate(orderDate);
        createBookingData.setOrderStatus("RESERVED");
        createBookingData.setOrderDuration(period.getDays() * 24L);
        createBookingData.setMaxRoomNumber(maxRooms);
        createBookingData.setBookingRefId(refId.toString());
        createBookingData.setRoomPrice(room.get().getRoom_price());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String queuePayloadString = objectMapper.writeValueAsString(createBookingData);
        System.out.println(queuePayloadString);
        rabbitTemplate.convertAndSend("","q.room-booking",queuePayloadString);

        res.put("success","true");
        res.put("booking_ref_id", refId);
        return CompletableFuture.completedFuture(res);
    }

    public Map<String, Map<String ,String>> getRoomAndHotelInfo(Long hotelId, Long roomCategoryId) throws Exception {
        Hotels hotel = hotelService.getHotel(hotelId,"");
        Optional<Rooms> room = roomsRepo.findRoomById(roomCategoryId);
        Map<String, String> data = new HashMap<>();
        Map<String, Map<String, String>> res = new HashMap<>();
        data.put("hotelName",hotel.getHotel_name());
        data.put("roomName", room.get().getroom_category_name());
        data.put("hotelAddress", hotel.getHotel_address());
        data.put("hotelCountry", hotel.getCountry());
        data.put("hotelState", hotel.getState());
        res.put("data", data);
        return res;
    }
}
