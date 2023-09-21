package com.kenneth.lotto.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table
public class Client {
    private static final int maxPicks = 6;
    private static final int minPick = 1;
    private static final int maxPick = 45;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="client_picks_id")
    private int id;
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
