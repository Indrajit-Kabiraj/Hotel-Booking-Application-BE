package com.example.hotelservice.Controllers;

import com.example.hotelservice.Models.Ammenities;
import com.example.hotelservice.Models.Rooms;
import com.example.hotelservice.Services.AmmenityService.AmmenityService;
import com.example.hotelservice.Services.AmmenityService.AmmenityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/v1/ammennities/")
public class AmmenityController {

    @Autowired
    private final AmmenityServiceImpl ammenityService;

    public AmmenityController(AmmenityServiceImpl ammenityService){
        this.ammenityService = ammenityService;
    }

    @GetMapping("/_search")
    @Async
    public CompletionStage<ResponseEntity<List<Ammenities>>> getAmmenities(){
        return ammenityService.getAmmenities().thenApplyAsync(res-> new ResponseEntity<List<Ammenities>>(res, HttpStatus.OK));
    }

    @PostMapping("/create")
    @Async
    public CompletionStage<ResponseEntity<Object>> addAmmenity(@RequestBody Ammenities ammenities){
        try{
            return ammenityService.createAmmenities(ammenities).thenApplyAsync(res->new ResponseEntity<Object>(res,HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }

    @PatchMapping("/update/{id}")
    @Async
    public CompletionStage<ResponseEntity<Object>> updateAmmenuty(@PathVariable Long id, String ammenityName){
        try{
            return ammenityService.updateAmmenity(id, ammenityName).thenApplyAsync(res->new ResponseEntity<Object>(res,HttpStatus.OK));
        } catch (Exception e){
            ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            return CompletableFuture.completedFuture(objectResponseEntity);
        }
    }
}
