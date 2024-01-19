package com.example.hotelservice.Services;

import com.example.hotelservice.Models.Ammenities;
import com.example.hotelservice.Models.RoomAmmenities;
import com.example.hotelservice.Repo.RoomAmmenitiesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RoomAmmenityService {

    @Autowired
    RoomAmmenitiesRepo roomAmmenitiesRepo;
    public void createRoomAmmenities(Long roomCategoryId, List<Ammenities> ammenities) {
        ammenities.forEach(ammenity->{
            addRoomAmmenity(roomCategoryId,ammenity);
        });
    }

    private void addRoomAmmenity(Long roomCategoryId, Ammenities ammenity) {
        RoomAmmenities roomAmmenities = new RoomAmmenities(roomCategoryId,ammenity.getId(), ammenity.getAmmenity_name());
        roomAmmenitiesRepo.save(roomAmmenities);
    }

    public List<String> getAmmenitiesNameByRoomId(Long roomCategoryId) {
        List<String> roomAmmenities = roomAmmenitiesRepo.findAllByRoomId(roomCategoryId).stream().map(roomAmm -> roomAmm.getAmmenityName()).collect(Collectors.toList());
        return roomAmmenities;
    }

    public void updateRoomAmmenities(Long roomCategoryId, List<Ammenities> ammenities) {
        List<RoomAmmenities> roomAmmenities = roomAmmenitiesRepo.findAllByRoomId(roomCategoryId);
        List<RoomAmmenities> leftDiff = roomAmmenities.stream().filter(rm -> !(ammenities).toString().contains(rm.getAmmenityName().toString())).collect(Collectors.toList());
        List<Ammenities> rightDiff = new ArrayList<>();
        ammenities.forEach(amm -> {
            int flag = 1;
            for(int i=0;i<roomAmmenities.size();i++){
                if(roomAmmenities.get(i).getAmmenityId() == amm.getId())
                    flag = 0;
            }
            if(flag == 1){
                rightDiff.add(amm);
            }
        });
        System.out.println(leftDiff);
        System.out.println(rightDiff);
        leftDiff.forEach(rma -> {
            roomAmmenitiesRepo.deleteByRoomAndAmmID(rma.getRoomCategoryId(),rma.getAmmenityId());
        });
        rightDiff.forEach(rma->{
            RoomAmmenities newRoomAmmenities = new RoomAmmenities(roomCategoryId,rma.getId(), rma.getAmmenity_name());
            roomAmmenitiesRepo.save(newRoomAmmenities);
        });
    }
}
