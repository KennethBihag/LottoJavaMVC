package com.kenneth.lotto.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table
public class Client implements LottoModel {
    private static final int minPick = 1, maxPick = 45;
    @Id
    @Column(name="client_picks_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="client_name",columnDefinition = "VARCHAR(45)")
    private String name;
    @Column(name = "client_picks",columnDefinition = "VARCHAR(45)")
    @Convert(converter =  IntArrToJsonConverter.class)
    private int[] picks;
    public Client(){}
    public Client(String name,int[] picks) throws IllegalArgumentException{
        if(picks == null || picks.length != maxPicks)
            throw new IllegalArgumentException(
                    String.format("Picks must only be of %d numbers.",maxPicks));
        boolean invalidPicks = Arrays.stream(picks).asLongStream().anyMatch(
                pick -> (pick<minPick || pick>maxPick));
        if(invalidPicks)
            throw new IllegalArgumentException(
                    String.format("Picks must be in the range of %d-%d only.",minPick,maxPick));
        if(name.isBlank())
            throw new IllegalArgumentException("Client name can't be empty.");
        this.name = name;
        this.picks = picks;
    }
    @Override
    public boolean equals(Object o){
        return id == ((Client)o).id;
    }
    @Override
    public String toString(){
        return String.format(
                "%s with ID %d picked %s",
                name,id,getPicksString()
        );
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int[] getPicks() {
        return Arrays.copyOf(picks,maxPicks);
    }

    @Override
    public void setPicks(int[] picks) {
        this.picks = picks;
    }
}
