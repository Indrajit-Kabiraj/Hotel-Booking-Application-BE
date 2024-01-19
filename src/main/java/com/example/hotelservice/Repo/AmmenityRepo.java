package com.example.hotelservice.Repo;

import com.example.hotelservice.Models.Ammenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmmenityRepo extends JpaRepository<Ammenities,Long> {

}
