package com.kenneth.lotto.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table
public class Client implements LottoModel {
    @Id
    @Column(name="client_picks_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Override
    public boolean equals(Object o){
        return id == ((Client)o).id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
    @Override
    public String toString(){
        return String.format(
                "%s with ID %d picked %s",
                name,id,getPicksString()
        );
    }
    @Override
    public String getPicksString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<maxPicks;++i) {
            sb.append(picks[i]);
            if(i<maxPicks-1)
                sb.append('-');
        }
        return sb.toString();
    }
}
