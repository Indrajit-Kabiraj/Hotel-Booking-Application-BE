package com.example.hotelservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@AllArgsConstructor
public class Ammenities {
    @Id
    @SequenceGenerator(name="idSequenceAmmenities", sequenceName="ID_SEQUENCE_AMMENITIES", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSequenceAmmenities")
    private long id;

    @NotNull
    private String ammenity_name;

    public Ammenities(String ammenity_name) {
        this.ammenity_name = ammenity_name;
    }

    public Ammenities(){
    }

    @Override
    public String toString() {
        return "Ammenities{" +
                "id=" + id +
                ", ammenity_name='" + ammenity_name + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAmmenity_name() {
        return ammenity_name;
    }

    public void setAmmenity_name(String ammenity_name) {
        this.ammenity_name = ammenity_name;
    }

}
