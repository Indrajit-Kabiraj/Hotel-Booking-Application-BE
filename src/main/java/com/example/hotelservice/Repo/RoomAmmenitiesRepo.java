package com.example.hotelservice.Repo;

import com.example.hotelservice.Models.RoomAmmenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAmmenitiesRepo extends JpaRepository<RoomAmmenities,Long> {
    @Query("SELECT r FROM RoomAmmenities r WHERE r.roomCategoryId = ?1 ORDER BY r.id desc")
    List<RoomAmmenities> findAllByRoomId(Long roomCategoryId);
    @Modifying
    @Query("delete from RoomAmmenities r where r.roomCategoryId=:roomCategoryId and r.ammenityId=:ammenityId")
    void deleteByRoomAndAmmID(@Param("roomCategoryId") Long roomCategoryId, @Param("ammenityId") Long ammenityId);
}


