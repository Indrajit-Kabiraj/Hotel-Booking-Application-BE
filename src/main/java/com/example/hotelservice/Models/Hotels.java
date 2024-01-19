package com.example.hotelservice.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@JsonIgnoreProperties(value= {"roomsList"})
public class Hotels implements Serializable {
    @Id
    @SequenceGenerator(name="idSequence", sequenceName="ID_SEQUENCE", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSequence")
    private Long id;
    @NotNull
    private String hotel_name;
    @NotNull
    private String hotel_address;
    @NotNull
    private String country;
    @NotNull

    private String state;

    private String img_location;
    private String hotel_company;

    private String status;

    private float rating;

    public Hotels() {

    }

    public Hotels(String hotel_name, String hotel_address, String country, String state, String img_location, String hotel_company, float rating) {
        this.hotel_name = hotel_name;
        this.hotel_address = hotel_address;
        this.country = country;
        this.state = state;
        this.img_location = img_location;
        this.hotel_company = hotel_company;
        this.rating = 0.0f;
        this.status = "ACTIVE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getHotel_address() {
        return hotel_address;
    }

    public void setHotel_address(String hotel_address) {
        this.hotel_address = hotel_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImg_location() {
        return img_location;
    }

    public void setImg_location(String img_location) {
        this.img_location = img_location;
    }

    public String getHotel_company() {
        return hotel_company;
    }

    public void setHotel_company(String hotel_company) {
        this.hotel_company = hotel_company;
    }

    @Override
    public String toString() {
        return "Hotels{" +
                "id=" + id +
                ", hotel_name='" + hotel_name + '\'' +
                ", hotel_address='" + hotel_address + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", img_location='" + img_location + '\'' +
                ", hotel_company='" + hotel_company + '\'' +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                '}';
    }
}
