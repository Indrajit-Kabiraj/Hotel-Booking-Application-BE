package com.example.hotelservice.Repo;

import com.example.hotelservice.Models.Hotels;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepo extends JpaRepository<Hotels, Long> {

    @Query("SELECT h FROM Hotels h WHERE h.country = ?1 AND h.state = ?2 AND h.status <> 'ARCHIVED' order by h.id")
    Optional<List<Hotels>> findByCountryAndState(String country, String state);

    @Query("SELECT h FROM Hotels h WHERE h.id in (?1) AND h.status <> 'ARCHIVED' order by h.id")
    List<Hotels> getAllHotelsById(List<Long> availableHotels);

    @Query("SELECT h.id, r.room_category_id, r.num_of_rooms FROM Hotels h INNER JOIN Rooms r ON h.id = r.hotel_id WHERE h.country = ?1 AND h.state = ?2 AND h.status <> 'ARCHIVED' order by h.id")
    List<Tuple> findHotelsAndRoomsByContry(String country, String state);
}
