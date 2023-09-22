package com.kenneth.lotto.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table
public class Client {
    public static final int maxPicks = 6;
    public static final int minPick = 1;
    public static final int maxPick = 45;

    @Column(name="client_picks_id",columnDefinition = "SERIAL")
    private int id;

    @Id
    @Column(name="client_name")
    private String name;
    @Column(name = "client_picks")
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
        return name.equals( ((Client)o).name );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getPicks() {
        return Arrays.copyOf(picks,maxPicks);
    }

    public void setPicks(int[] picks) {
        this.picks = picks;
    }
}
