package com.example.hotelservice.Services.AmmenityService;

import com.example.hotelservice.Models.Ammenities;
import com.example.hotelservice.Repo.AmmenityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Service
public class AmmenityServiceImpl {

    @Autowired
    AmmenityRepo ammenityRepo;

    AmmenityServiceImpl(AmmenityRepo ammenityRepo){
        this.ammenityRepo = ammenityRepo;
    }

    public CompletionStage<List<Ammenities>> getAmmenities(){
        return CompletableFuture.completedFuture(ammenityRepo.findAll());
    }

    public CompletionStage<Object> createAmmenities(Ammenities ammenities){
        ammenityRepo.save(ammenities);
        return CompletableFuture.completedFuture(ammenities);
    }

    public CompletionStage<Object> updateAmmenity(Long id, String ammenityName){
        try{
            Optional<Ammenities> ammenity = ammenityRepo.findById(id);
            if(ammenity.isPresent()){
                ammenity.get().setAmmenity_name(ammenityName);
                ammenityRepo.save(ammenity.get());
                return CompletableFuture.completedFuture(ammenity.get());
            }
            else{
                throw new Exception("Ammenity Not Found!");
            }
        } catch (Exception e){
            return CompletableFuture.completedFuture(e.getMessage());
        }
    }
}
