package com.example.hotelservice.Services.HotelService;

import com.example.hotelservice.DTOs.HotelResponseDTO;
import com.example.hotelservice.DTOs.HotelSearchDTO;
import com.example.hotelservice.DTOs.UserHotelRatingDTO;
import com.example.hotelservice.DTOs.VacantRoomsDTO;
import com.example.hotelservice.Exceptions.HotelsNotFoundException;
import com.example.hotelservice.Models.Hotels;
import com.example.hotelservice.Repo.HotelRepo;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "hotelCache")
public class HotelServiceImpl {


    private final HotelRepo hotelsRepo;

//    @Cacheable(key = "#state", condition="#state!=null")
    public CompletionStage<HotelResponseDTO> getHotelByCountryAndState(String country, String state) throws HotelsNotFoundException, IllegalStateException{
        Optional<List<Hotels>> hotels = hotelsRepo.findByCountryAndState(country, state);
        Map<String,List<Hotels>> res = new HashMap<>();
        if(hotels.isPresent()){
            HotelResponseDTO resp = new HotelResponseDTO(Long.valueOf(hotels.get().size()), hotels.get());
            return CompletableFuture.completedFuture(resp);
        }
        else if( !hotels.isPresent() ){
            throw new HotelsNotFoundException("No hotels avaialble!");
        } else {
            throw new IllegalStateException("Illegally formatted request!");
        }
    }

    @Transactional
    public CompletionStage<List<Hotels>> getAvailableHotelsByCountryAndState(HotelSearchDTO searchQuery) throws HotelsNotFoundException{
        List<Tuple> hotels = hotelsRepo.findHotelsAndRoomsByContry(searchQuery.getCountry(), searchQuery.getState());
        List<VacantRoomsDTO> allHotelsByCountry = hotels.stream()
                .map(t -> new VacantRoomsDTO(
                        t.get(0, Long.class),
                        t.get(1, Long.class),
                        t.get(2, Long.class)
                ))
                .collect(Collectors.toList());
        List<Long> hotelIds = allHotelsByCountry.stream().map(hotel -> hotel.getHotelId()).distinct().collect(Collectors.toList());
        System.out.println(hotelIds);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String URL = "http://localhost:8082/v1/bookings/_search/vacantHotels";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("hotelId",hotelIds);
        jsonObj.put("startTime", searchQuery.getStartTime());
        jsonObj.put("endTime",searchQuery.getEndTime());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObj.toString());
        List<VacantRoomsDTO> bookedHotels = new ArrayList<>();
        Request request = new Request.Builder().url(URL).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            String resp = response.body().string();
            JSONObject json = new JSONObject(resp);

            JSONArray respArray = json.getJSONArray("bookedRoons");
            for(int i=0;i<respArray.length();i++){
                VacantRoomsDTO val = new VacantRoomsDTO(Long.parseLong(respArray.getJSONObject(i).get("hotelId").toString()), Long.parseLong(respArray.getJSONObject(i).get("roomCategoryId").toString()), Long.parseLong(respArray.getJSONObject(i).get("numberOfVacantRooms").toString()));
                bookedHotels.add(val);
            }
//            System.out.println(bookedHotels);
            List<Long> availableHotels = new ArrayList<>(hotelIds);
            /* TODO - Make this logic efficient */
            for(int i = 0; i < allHotelsByCountry.size(); i++){

                for( int j = 0 ; j < bookedHotels.size() ; j++){
                    if((bookedHotels.get(j).getHotelId() == allHotelsByCountry.get(i).getHotelId()) &&
                            (bookedHotels.get(j).getRoomCategoryId() == allHotelsByCountry.get(i).getRoomCategoryId())){
                        if(bookedHotels.get(j).getNumberOfBookedRooms() >= allHotelsByCountry.get(i).getNumberOfBookedRooms()){
                            availableHotels.remove(allHotelsByCountry.get(i).getHotelId());
                        }
                    }
                }
            }
//            System.out.println(availableHotels);
            if(availableHotels.size() == 0){
                throw new HotelsNotFoundException("No available hotels!");
            }
            return CompletableFuture.completedFuture(hotelsRepo.getAllHotelsById(availableHotels));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Cacheable(key = "#id", condition="#id!=null")
    public Hotels getHotel(Long hotelId, String country) throws HotelsNotFoundException, Exception {
        try {
            Optional<Hotels> hotel = hotelsRepo.findById(hotelId);
            if(hotel.isPresent()){
                return hotel.get();
            } else{
                throw new HotelsNotFoundException("No available hotel with the ID: " + hotelId.toString());
            }
        } catch (Exception e){
            throw new Exception();
        }
    }

    @Transactional
    public Map<String,Hotels> addHotel(Hotels hotel){
//        hotelUtils.validate(hotel);
        hotel.setStatus("ACTIVE");
        hotel.setRating(0.0f);
        hotelsRepo.save(hotel);
        Map<String, Hotels> res = new HashMap<>();
        res.put("data",hotel);
        return res;
    }

    @Transactional
//    @CachePut(key = "#id", condition="#id!=null")
    public Object updateHotel(Hotels hotel, Long id){
        Optional<Hotels> findHotel = hotelsRepo.findById(id);
        Map<String, String> res = new HashMap<>();
        if(findHotel.isPresent() == false){
            res.put("error_message","Hotel doesn't exist!");
            res.put("update_status", "FAILED");
            return res;
        }
        Hotels existingHotel = findHotel.get();
        existingHotel = updateHotelDetails(existingHotel,hotel);
        hotelsRepo.save(existingHotel);
        res.put("hotel_updated", existingHotel.getHotel_name());
        res.put("update_status", "SUCCESS");
        res.put("hotel", String.valueOf(hotel));
        return res;
    }

    Hotels updateHotelDetails(Hotels existingHotel, Hotels hotel){
        if(hotel.getHotel_name()!=null && hotel.getHotel_name() != existingHotel.getHotel_name()) existingHotel.setHotel_name(hotel.getHotel_name());
        if(hotel.getHotel_address()!=null && hotel.getHotel_address() != existingHotel.getHotel_address()) existingHotel.setHotel_address(hotel.getHotel_address());
        if(hotel.getHotel_company()!=null && hotel.getHotel_company() != existingHotel.getHotel_company()) existingHotel.setHotel_company(hotel.getHotel_company());
        if(hotel.getState() != null && hotel.getState() != existingHotel.getState()) existingHotel.setState(hotel.getState());
        if(hotel.getCountry()!=null && hotel.getCountry() != existingHotel.getCountry()) existingHotel.setCountry(hotel.getCountry());
        if(hotel.getImg_location()!=null && hotel.getImg_location() != existingHotel.getImg_location()) existingHotel.setImg_location(hotel.getImg_location());
        if(hotel.getRating() != existingHotel.getRating()) existingHotel.setRating(hotel.getRating());
        if(hotel.getStatus() != null && hotel.getStatus() != existingHotel.getStatus()) existingHotel.setStatus(hotel.getStatus());
        return existingHotel;
    }

    @CacheEvict(key = "#id", beforeInvocation = true)
    public Object deleteHotel(Long id){
        Optional<Hotels> findHotel = hotelsRepo.findById(id);
        Map<String, String> res = new HashMap<>();
        if(findHotel.isPresent() == false){
            res.put("error_message","Hotel doesn't exist!");
            res.put("deletion_status", "FAILED");
        }
        Hotels existingHotel = findHotel.get();
        existingHotel.setStatus("ARCHIVED");
        hotelsRepo.save(existingHotel);
        res.put("hotel_updated", existingHotel.getHotel_name());
        res.put("deletion_status", "SUCCESS");
        return res;
    }


    public Map<String, String> updateRatingForAHotel(UserHotelRatingDTO review) {
        Optional<Hotels> existingHotel = hotelsRepo.findById(review.getHotelId());
        float oldRating = existingHotel.get().getRating();
        float newRating = (oldRating + review.getRating())/ review.getCountOfRatings();
        existingHotel.get().setRating(newRating);
        hotelsRepo.save(existingHotel.get());
        Map<String, String> resp = new HashMap<>();
        resp.put("hotelId", review.getHotelId().toString());
        resp.put("newRating", String.valueOf(existingHotel.get().getRating()));
        resp.put("rating_update", "SUCCESS");
        return resp;
    }
}
