package com.example.hotelservice.Repo;

import com.example.hotelservice.Models.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomsRepo extends JpaRepository<Rooms, Long> {

    @Query("SELECT r FROM Rooms r WHERE r.room_category_id = ?1 AND r.room_status <> 'ARCHIVED'")
    Optional<Rooms> findRoomById(Long roomId);

    @Query("SELECT r FROM Rooms r WHERE r.hotel_id = ?1 AND r.room_status <> 'ARCHIVED'")
    List<Rooms> findRoomsByHotelId(Long hotelId);

    @Query("SELECT r FROM Rooms r WHERE r.room_category_id in (?1) AND r.room_status <> 'ARCHIVED'")
    List<Rooms> findRoomsByIds(List<Long> availableRooms);
}
